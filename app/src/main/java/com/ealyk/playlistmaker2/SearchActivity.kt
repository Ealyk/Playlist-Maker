package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class SearchActivity : AppCompatActivity() {

    private var savedEditText = DEF_TEXT

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val backButton = findViewById<Button>(R.id.back_button)
        val editTextSearch = findViewById<EditText>(R.id.searching)
        val clearButton = findViewById<FrameLayout>(R.id.clear_button)
        val rootLayout = findViewById<LinearLayout>(R.id.search_layout)
        val rvTrackSearch = findViewById<RecyclerView>(R.id.recyclerTrackSearch)

        if (savedInstanceState != null) {
            savedEditText = savedInstanceState.getString(EDITABLE_TEXT, DEF_TEXT)
            editTextSearch.setText(savedEditText)

        }

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            editTextSearch.setText("")
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

        rvTrackSearch.adapter = TrackAdapter(
            listOf (
                Track (
                    "Smells Like Teen Spirit",
                    "Nirvana",
                    "5:01",
                    getString(R.string.Smells_Like_Teen_Spirit_uri)
                ),
                Track (

                    "Billie Jean",
                    "Michael Jackson",
                    "4:35",
                    getString(R.string.Billie_Jean_uri)
                ),
                Track (
                    "Stayin' Alive",
                    "Bee Gees",
                    "4:10",
                    getString(R.string.Stayin_Alive_uri)
                ),
                Track (
                    "Whole Lotta Love",
                    "Led Zeppelin",
                    "5:33",
                    getString(R.string.Whole_Lotta_Love_uri)
                ),
                Track (
                    "Sweet Child O'Mine",
                    "Guns N' Roses",
                    "5:03",
                    getString(R.string.Sweet_Child_uri)
                )
            )
        )


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

    companion object {
        private const val EDITABLE_TEXT = "EDITABLE_TEXT"
        private const val DEF_TEXT = ""
    }

}
