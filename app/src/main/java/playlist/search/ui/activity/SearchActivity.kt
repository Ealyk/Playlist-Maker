package playlist.search.ui.activity

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivitySearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import playlist.search.ui.adapter.TrackAdapter
import playlist.search.domain.model.TrackSearchState
import playlist.search.ui.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private val searchRunnable by lazy { Runnable { viewModel.loadTrack(binding.searching.text.toString()) } }

    private lateinit var adapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        adapter = TrackAdapter(
            onTrackClicked = { track ->
                viewModel.onTrackClicked(track)
                binding.searching.setText(DEF_TEXT)
                             },
            this
        )
        historyAdapter = TrackAdapter(
            onTrackClicked = { track ->
                viewModel.onTrackClicked(track)
                binding.searching.setText(DEF_TEXT) },
            this
        )

        binding.recyclerSearchHistory.adapter = historyAdapter
        binding.recyclerTrackSearch.adapter = adapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearButton.setOnClickListener {

            handler.removeCallbacks(searchRunnable)
            binding.searching.setText(DEF_TEXT)
            viewModel.clearSearch()
            adapter.notifyDataSetChanged()

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchLayout.windowToken, 0)
        }

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            historyAdapter.notifyDataSetChanged()
        }


        binding.searching.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searching.text.isEmpty() && historyAdapter.trackUiList.isNotEmpty()) showHistoryList()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
                binding.clearButton.visibility = clearButtonVisibility(s ?: "")
                if (binding.searching.hasFocus() && s?.isEmpty() == true) showHistoryList()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        binding.searching.addTextChangedListener(textWatcher)



        binding.searching.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.searchLayout.windowToken, 0)

                true
            } else false
        }

        binding.reloadButton.setOnClickListener {

            viewModel.loadTrack(binding.searching.text.toString())
        }

        viewModel.observeStateSearching().observe(this) {
            render(it)
        }

    }

    private fun clearButtonVisibility(s:CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        }
        else{
            View.VISIBLE

        }
    }

    private fun render(state: TrackSearchState) {
        when(state) {
            is TrackSearchState.Loading -> showLoading()
            is TrackSearchState.Content -> {
                adapter.updateList(state.data)
                showTrackList()
            }
            is TrackSearchState.Error -> showErrorState()
            is TrackSearchState.Empty -> showEmptyState()
            is TrackSearchState.History -> {
                historyAdapter.updateList(state.history)
                showHistoryList()
            }
        }
    }

    private fun showHistoryList() {
        binding.emptyStateLayout.isVisible = false
        binding.errorStateLayout.isVisible = false
        binding.recyclerTrackSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.containerHistory.isVisible = (historyAdapter.trackUiList).isNotEmpty()
    }

    private fun showEmptyState() {
        binding.emptyStateLayout.isVisible = true
        binding.errorStateLayout.isVisible = false
        binding.recyclerTrackSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.containerHistory.isVisible = false
    }

    private fun showErrorState() {
        binding.emptyStateLayout.isVisible = false
        binding.errorStateLayout.isVisible = true
        binding.recyclerTrackSearch.isVisible = false
        binding.progressBar.isVisible = false
        binding.containerHistory.isVisible = false
    }

    private fun showTrackList() {
        binding.emptyStateLayout.isVisible = false
        binding.errorStateLayout.isVisible = false
        binding.recyclerTrackSearch.isVisible = true
        binding.progressBar.isVisible = false
        binding.containerHistory.isVisible = false
    }

    private fun showLoading() {
        binding.emptyStateLayout.isVisible = false
        binding.errorStateLayout.isVisible = false
        binding.recyclerTrackSearch.isVisible = false
        binding.progressBar.isVisible = true
        binding.containerHistory.isVisible = false
    }



    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val DEF_TEXT = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}
