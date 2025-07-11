package playlist.main.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivityMainBinding
import playlist.main.ui.NavigationClient
import playlist.main.ui.impl.NavigationClientImpl
import playlist.main.ui.view_model.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var viewModel: MainViewModel? = null
    private lateinit var navigationClient: NavigationClient

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
        navigationClient = NavigationClientImpl(this)

        viewModel = ViewModelProvider(this, MainViewModel.getFactory(navigationClient))
            .get(MainViewModel::class.java)

        binding.buttonSearch.setOnClickListener {
            viewModel?.onSearchClicked()
        }
        binding.buttonMedia.setOnClickListener {
                viewModel?.onMediaClicked()
            }

        binding.buttonSettings.setOnClickListener {
            viewModel?.onSettingsClicked()
        }
    }


}

