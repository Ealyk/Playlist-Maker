package ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ealyk.playlistmaker2.Creator
import com.ealyk.playlistmaker2.R
import com.google.android.material.switchmaterial.SwitchMaterial
import domain.api.ThemeSwitcher


class SettingsActivity : AppCompatActivity() {

    private lateinit var switch: SwitchMaterial
    private lateinit var provideThemeSwitcher: ThemeSwitcher

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        switch = findViewById(R.id.switch_theme)
        provideThemeSwitcher = Creator.provideThemeSwitcher(this)

        switch.isChecked = provideThemeSwitcher.loadTheme()

        switch.setOnCheckedChangeListener { switcher, checked ->
            provideThemeSwitcher.switchTheme(checked)
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


}