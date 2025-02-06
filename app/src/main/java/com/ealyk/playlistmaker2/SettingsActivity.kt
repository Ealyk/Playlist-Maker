package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        val shareFrameLayout = findViewById<FrameLayout>(R.id.share)
        shareFrameLayout.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            val shareText = arrayOf(getString(R.string.share_uri))
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }
        val supportFrameLayout = findViewById<FrameLayout>(R.id.support)
        supportFrameLayout.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_support))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_support))
            startActivity(supportIntent)
        }
        val agreementFrameLayout = findViewById<FrameLayout>(R.id.agreement)
        agreementFrameLayout.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.agreement_uri))
            startActivity(agreementIntent)


        }
        val switch = findViewById<Switch>(R.id.switch_theme)

        fun saveThemePreference(isDarkMode: Boolean) {
            val sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("is_dark_mode", isDarkMode).apply()
        }
        fun loadThemePreference(): Boolean {
            val sharedPreferences = getSharedPreferences("theme_prefs", MODE_PRIVATE)
            return sharedPreferences.getBoolean("is_dark_mode", false)
        }
        fun switchTheme(isDarkMode: Boolean) {
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        switch.isChecked = loadThemePreference()
        switch.setOnCheckedChangeListener { _, isChecked ->
            switchTheme(isChecked)
            saveThemePreference(isChecked)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}