package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchActivity : AppCompatActivity() {

    private var savedEditText = DEF_TEXT


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Log.d("MySerchLog", "SearchActivity Создан")

        val backButton = findViewById<Button>(R.id.back_button)
        val editTextSearch = findViewById<EditText>(R.id.searching)
        val clearButton = findViewById<FrameLayout>(R.id.clear_button)
        val rootLayout = findViewById<LinearLayout>(R.id.search_layout)

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
            Log.d("MySerchLog", "Нажали крестик")
        }



        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s ?: "")
                Log.d("MySerchLog", "Видимость крестика")
            }

            override fun afterTextChanged(s: Editable?) {

                 savedEditText = s.toString()
            }

        }
        editTextSearch.addTextChangedListener(textWatcher)

    }

    override fun onStart() {
        super.onStart()
        Log.d("MySerchLog", "onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedEditText = savedInstanceState.getString(EDITABLE_TEXT, DEF_TEXT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDITABLE_TEXT, savedEditText)
    }

    override fun onPause() {
        super.onPause()
        Log.d("MySerchLog", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MySerchLog", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MySerchLog", "onDestroy")
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
        const val EDITABLE_TEXT = "EDITABLE_TEXT"
        const val DEF_TEXT = ""
    }

}
