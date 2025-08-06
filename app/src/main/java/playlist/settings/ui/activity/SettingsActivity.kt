package playlist.settings.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivitySettingsBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import playlist.settings.ui.view_model.SettingsViewModel
import playlist.sharing.domain.SharingInteractor


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val sharingInteractor: SharingInteractor by inject()
    private val viewModel: SettingsViewModel by viewModel()


    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchTheme.isChecked = viewModel.getCurrentTheme().isDark

        viewModel.observeTheme().observe(this) {
            binding.switchTheme.isChecked = it.isDark
        }


        binding.switchTheme.setOnCheckedChangeListener { switcher, checked ->
            viewModel.changeTheme(checked)
        }


        binding.backButton.setOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            viewModel.onShareClicked()
        }

        binding.support.setOnClickListener {
            viewModel.onSupportClicked()
        }

        binding.agreement.setOnClickListener {
            viewModel.onTermsClicked()
        }
    }


}