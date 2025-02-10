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



    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switch = findViewById<Switch>(R.id.switch_theme)

        fun saveThemePreference(isDarkMode: Boolean) {
            val sharedPreferences = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(IS_DARK_MODE, isDarkMode).apply()
        }
        fun loadThemePreference(): Boolean {
            val sharedPreferences = getSharedPreferences(THEME_PREFS, MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_DARK_MODE, DEF_IS_DARK)
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
    }



    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object{
        private val THEME_PREFS = "theme_prefs"
        private val IS_DARK_MODE = "is_dark_mode"
        private val DEF_IS_DARK = false
    }
}