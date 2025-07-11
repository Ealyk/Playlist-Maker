package playlist.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivitySettingsBinding
import playlist.settings.ui.view_model.SettingsViewModel



class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var viewModel: SettingsViewModel? = null


    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        binding.switchTheme.isChecked = viewModel?.getCurrentTheme()?.isDark ?: false

        viewModel?.observeTheme()?.observe(this) {
            binding.switchTheme.isChecked = it.isDark
        }


        binding.switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel?.changeTheme(checked)
        }


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            viewModel?.onShareClicked()
        }

        binding.support.setOnClickListener {
            viewModel?.onSupportClicked()
        }

        binding.agreement.setOnClickListener {
            viewModel?.onTermsClicked()
        }
    }


}