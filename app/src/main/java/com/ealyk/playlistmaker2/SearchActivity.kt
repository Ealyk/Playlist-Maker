package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private var savedEditText = DEF_TEXT


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

    private val trackList = ArrayList<Track>()

    private val adapter = TrackAdapter(trackList)

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
            adapter.notifyDataSetChanged()
            showTrackList(emptyStateLayout, errorStateLayout, rvTrackSearch)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(rootLayout.windowToken, 0)
        }



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s ?: "")
            }

            override fun afterTextChanged(s: Editable?) {

                 savedEditText = s.toString()
            }

        }
        editTextSearch.addTextChangedListener(textWatcher)

        rvTrackSearch.adapter = adapter

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

    companion object {
        private const val iTunesbaseUrl = "https://itunes.apple.com"
        private const val EDITABLE_TEXT = "EDITABLE_TEXT"
        private const val DEF_TEXT = ""
    }

}
