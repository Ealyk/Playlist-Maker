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

    private val iTunesbaseUrl = "https://itunes.apple.com"

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
            showTrackList()
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
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            showEmptyState()
                        }
                        else showTrackList()
                    }
                    else {
                        showErrorState()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showErrorState()
                    reloadButton.setOnClickListener {

                        recreate()
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
        findViewById<LinearLayout>(R.id.emptyStateLayout).visibility = View.VISIBLE
        findViewById<LinearLayout>(R.id.errorStateLayout).visibility = View.GONE
        findViewById<RecyclerView>(R.id.recyclerTrackSearch).visibility = View.GONE
    }

    private fun showErrorState() {
        findViewById<LinearLayout>(R.id.emptyStateLayout).visibility = View.GONE
        findViewById<LinearLayout>(R.id.errorStateLayout).visibility = View.VISIBLE
        findViewById<RecyclerView>(R.id.recyclerTrackSearch).visibility = View.GONE
    }

    private fun showTrackList() {
        findViewById<LinearLayout>(R.id.emptyStateLayout).visibility = View.GONE
        findViewById<LinearLayout>(R.id.errorStateLayout).visibility = View.GONE
        findViewById<RecyclerView>(R.id.recyclerTrackSearch).visibility = View.VISIBLE
    }

    companion object {
        private const val EDITABLE_TEXT = "EDITABLE_TEXT"
        private const val DEF_TEXT = ""
    }

}
