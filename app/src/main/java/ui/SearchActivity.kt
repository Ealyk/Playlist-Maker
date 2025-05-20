package ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ealyk.playlistmaker2.Creator
import com.ealyk.playlistmaker2.R
import presentation.TrackAdapter
import domain.api.HistoryInteractor
import domain.api.HistoryObserver
import domain.api.TrackInteractor
import domain.models.Track


class SearchActivity : AppCompatActivity(), HistoryObserver {


    private var savedEditText = DEF_TEXT

    private lateinit var handler: Handler

    private val searchRunnable = Runnable { loadTrack(savedEditText) }

    private lateinit var provideInteractor: TrackInteractor
    private lateinit var searchHistory: HistoryInteractor

    private lateinit var backButton: Button
    private lateinit var editTextSearch: EditText
    private lateinit var clearButton: FrameLayout
    private lateinit var rootLayout: LinearLayout
    private lateinit var rvTrackSearch: RecyclerView
    private lateinit var reloadButton: Button
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var errorStateLayout: LinearLayout
    private lateinit var historyLayout: LinearLayout
    private lateinit var rvSearchHistory: RecyclerView
    private lateinit var clearHistory: Button
    private lateinit var progressBar: ProgressBar

    private val trackList = mutableListOf<Track>()
    private val historyList = mutableListOf<Track>()

    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        backButton = findViewById(R.id.back_button)
        editTextSearch = findViewById(R.id.searching)
        clearButton = findViewById(R.id.clear_button)
        rootLayout = findViewById(R.id.search_layout)
        rvTrackSearch = findViewById(R.id.recyclerTrackSearch)
        reloadButton = findViewById(R.id.reloadButton)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        errorStateLayout = findViewById(R.id.errorStateLayout)
        historyLayout = findViewById(R.id.containerHistory)
        rvSearchHistory = findViewById(R.id.recyclerSearchHistory)
        clearHistory = findViewById(R.id.clear_history_button)
        progressBar = findViewById(R.id.progressBar)

        handler = Handler(Looper.getMainLooper())

        searchHistory = Creator.provideHistoryInteractor(this)
        provideInteractor = Creator.provideTrackInteractor()

        adapter = TrackAdapter(trackList, searchHistory, isHistory = false, this)
        historyAdapter = TrackAdapter(historyList, searchHistory, isHistory = true, this)

        rvSearchHistory.adapter = historyAdapter
        rvTrackSearch.adapter = adapter

        searchHistory.addObserver(this)
        historyList.addAll(searchHistory.loadHistory().toMutableList())
        historyAdapter.notifyDataSetChanged()
        historyLayout.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE

        if (savedInstanceState != null) {
            savedEditText = savedInstanceState.getString(EDITABLE_TEXT, DEF_TEXT)
            editTextSearch.setText(savedEditText)

        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {

            editTextSearch.setText(DEF_TEXT)
            trackList.clear()
            progressBar.isVisible = false

            val updateHistoryList = searchHistory.loadHistory().toMutableList()

            historyAdapter.updateList(updateHistoryList)
            historyLayout.isVisible = if (historyList.isEmpty()) false else true
            adapter.notifyDataSetChanged()

            showTrackList()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(rootLayout.windowToken, 0)
        }

        clearHistory.setOnClickListener {
            searchHistory.clearHistory()
            onHistoryUpdate(historyList)
            historyAdapter.notifyDataSetChanged()
        }



        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            historyLayout.isVisible = if (hasFocus && editTextSearch.text.isEmpty()) false else true
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                searchDebounce()
                clearButton.visibility = clearButtonVisibility(s ?: "")
                historyLayout.isVisible = if (editTextSearch.hasFocus() && s?.isEmpty() == true) {

                    true
                }
                else false
            }

            override fun afterTextChanged(s: Editable?) {

                 savedEditText = s.toString()
            }

        }
        editTextSearch.addTextChangedListener(textWatcher)



        editTextSearch.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(rootLayout.windowToken, 0)

                true
            } else false
        }

        reloadButton.setOnClickListener {

            errorStateLayout.isVisible = false
            progressBar.isVisible = true
            loadTrack(savedEditText)
        }

    }

    private fun loadTrack(query: String) {
        if (editTextSearch.text.isNotEmpty()) {
            progressBar.isVisible = true
            provideInteractor.searchTracks(editTextSearch.text.toString(), object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: Result<List<Track>>) {
                    runOnUiThread {
                        progressBar.isVisible = false

                        foundTracks.onSuccess { tracks ->
                            if (tracks.isNotEmpty()) {
                                showTrackList()
                                trackList.clear()
                                trackList.addAll(tracks)
                                adapter.notifyDataSetChanged()
                            } else {
                                showEmptyState()
                            }
                        }
                        foundTracks.onFailure {
                            showErrorState()
                        }
                    }
                }

            })
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEditText = savedInstanceState.getString(EDITABLE_TEXT, DEF_TEXT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDITABLE_TEXT, savedEditText)
    }

    private fun clearButtonVisibility(s:CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        }
        else{
            View.VISIBLE

        }
    }

    private fun showEmptyState() {
        emptyStateLayout.visibility = View.VISIBLE
        errorStateLayout.visibility = View.GONE
        rvTrackSearch.visibility = View.GONE
    }

    private fun showErrorState() {
        emptyStateLayout.visibility = View.GONE
        errorStateLayout.visibility = View.VISIBLE
        rvTrackSearch.visibility = View.GONE
    }

    private fun showTrackList() {
        emptyStateLayout.visibility = View.GONE
        errorStateLayout.visibility = View.GONE
        rvTrackSearch.visibility = View.VISIBLE
    }

    override fun onHistoryUpdate(historyList: List<Track>) {
        historyAdapter.updateList(historyList)
        historyLayout.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE

    }


    override fun onDestroy() {
        super.onDestroy()
        searchHistory.removeObserver(this)
    }

    private fun searchDebounce() {
        progressBar.isVisible = true
        adapter.updateList(trackList)
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    companion object {

        private const val EDITABLE_TEXT = "EDITABLE_TEXT"
        private const val DEF_TEXT = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}
