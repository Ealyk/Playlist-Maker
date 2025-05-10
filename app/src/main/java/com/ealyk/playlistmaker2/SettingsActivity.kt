package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    private lateinit var switch: SwitchMaterial

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switch = findViewById(R.id.switch_theme)

        val sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, MODE_PRIVATE)

        switch.isChecked = sharedPreferences.getBoolean(SWITCH_THEME_KEY, DEF_IS_DARK)

        switch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPreferences.edit().putBoolean(SWITCH_THEME_KEY, checked).apply()
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

    companion object {
        private const val DEF_IS_DARK = false
        private const val SWITCH_THEME_KEY = "switch key"
        private const val SHARED_PREF_KEY = "shared key"
    }

}