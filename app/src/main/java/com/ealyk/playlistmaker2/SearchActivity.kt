package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        Log.d("MySerchLog", "SearchActivity Создан")

        val backButton = findViewById<Button>(R.id.back_button)
        val editTextSearch = findViewById<EditText>(R.id.searching)
        val clearButton = findViewById<FrameLayout>(R.id.clear_button)

        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            editTextSearch.setText("")
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

            }

        }
        editTextSearch.addTextChangedListener(textWatcher)

    }

    private fun clearButtonVisibility(s:CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            Log.d("MySerchLog", "крестик не виден")
            View.GONE
        }
        else{
            Log.d("MySerchLog", "крестик виден")
            View.VISIBLE

        }
    }


}