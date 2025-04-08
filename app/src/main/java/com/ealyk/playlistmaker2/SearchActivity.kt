package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ealyk.playlistmaker2.Constants.DEF_TEXT
import com.ealyk.playlistmaker2.Constants.EDITABLE_TEXT
import com.ealyk.playlistmaker2.Constants.SHARED_PREF_KEY
import com.ealyk.playlistmaker2.Constants.iTunesbaseUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity(), HistoryObserver {

    private var savedEditText = DEF_TEXT

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesbaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)

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

    private val trackList = mutableListOf<Track>()
    private val historyList = mutableListOf<Track>()

    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)


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

        adapter = TrackAdapter(trackList, sharedPreferences, isHistory = false, this)
        historyAdapter = TrackAdapter(historyList, sharedPreferences, isHistory = true, this)

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

            editTextSearch.setText("")
            trackList.clear()

            val updateHistoryList = searchHistory.loadHistory().toMutableList()

            historyAdapter.updateList(updateHistoryList)
            historyLayout.visibility = if (historyList.isEmpty()) View.GONE else View.VISIBLE
            adapter.notifyDataSetChanged()

            showTrackList(emptyStateLayout, errorStateLayout, rvTrackSearch)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(rootLayout.windowToken, 0)
        }

        clearHistory.setOnClickListener {
            searchHistory.clearHistory()
            onHistoryUpdate(historyList)
            historyAdapter.notifyDataSetChanged()
        }



        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            historyLayout.visibility = if (hasFocus && editTextSearch.text.isEmpty()) View.GONE else View.VISIBLE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s ?: "")
                historyLayout.visibility = if (editTextSearch.hasFocus() && s?.isEmpty() == true) {

                    View.VISIBLE
                }
                else View.GONE
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

                performSearch(savedEditText)
                true
            } else false
        }

        reloadButton.setOnClickListener {
            performSearch(savedEditText)
        }

    }

    private fun performSearch(query: String) {
        if (editTextSearch.text.isNotEmpty()) {
            iTunesService.search(editTextSearch.text.toString()).enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {

                    val responseBody = response.body()?.results

                    if (response.code() == 200) {
                        trackList.clear()
                        if (responseBody?.isNotEmpty() == true) {
                            trackList.addAll(responseBody)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showEmptyState(emptyStateLayout, errorStateLayout, rvTrackSearch)
                        }
                        else showTrackList(emptyStateLayout, errorStateLayout, rvTrackSearch)
                    }
                    else {
                        showErrorState(emptyStateLayout, errorStateLayout, rvTrackSearch)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showErrorState(emptyStateLayout, errorStateLayout, rvTrackSearch)
                    reloadButton.setOnClickListener {

                        performSearch(query)
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

    private fun showEmptyState(
        emptyStateLayout: LinearLayout,
        errorStateLayout: LinearLayout,
        rvTrackSearch: RecyclerView) {
        emptyStateLayout.visibility = View.VISIBLE
        errorStateLayout.visibility = View.GONE
        rvTrackSearch.visibility = View.GONE
    }

    private fun showErrorState(
        emptyStateLayout: LinearLayout,
        errorStateLayout: LinearLayout,
        rvTrackSearch: RecyclerView) {
        emptyStateLayout.visibility = View.GONE
        errorStateLayout.visibility = View.VISIBLE
        rvTrackSearch.visibility = View.GONE
    }

    private fun showTrackList(
        emptyStateLayout: LinearLayout,
        errorStateLayout: LinearLayout,
        rvTrackSearch: RecyclerView) {
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


}
