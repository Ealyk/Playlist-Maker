package playlist.main.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import playlist.main.ui.NavigationClient
import playlist.main.ui.view_model.MainViewModel
import playlist.media.ui.activity.MediaActivity
import playlist.search.ui.activity.SearchActivity
import playlist.settings.ui.activity.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.navigateTo().observe(this) { destination ->
            when (destination) {
                is NavigationClient.Search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                }

                is NavigationClient.Media -> {
                    val intent = Intent(this, MediaActivity::class.java)
                    startActivity(intent)
                }

                is NavigationClient.Settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.buttonSearch.setOnClickListener {
            viewModel.onSearchClicked()
        }
        binding.buttonMedia.setOnClickListener {
                viewModel.onMediaClicked()
            }

        binding.buttonSettings.setOnClickListener {
            viewModel.onSettingsClicked()
        }
    }


}

