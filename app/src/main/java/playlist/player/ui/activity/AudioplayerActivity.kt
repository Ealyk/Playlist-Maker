package playlist.player.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ealyk.playlistmaker2.R
import com.ealyk.playlistmaker2.databinding.ActivityAudioplayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import playlist.search.ui.model.TrackUi
import playlist.player.domain.model.PlayerState
import playlist.player.ui.view_model.AudioplayerViewModel


class AudioplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel: AudioplayerViewModel by viewModel()

    private lateinit var trackUrl: String



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_audioplayer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackUi =  intent.getParcelableExtra<TrackUi>(EXTRA_TRACK)
        trackUrl = trackUi?.previewUrl ?: ""

        binding.country.text = trackUi?.country ?: ""
        binding.genre.text = trackUi?.primaryGenreName ?: ""
        binding.nameGroup.text = trackUi?.artistName ?: ""
        binding.trackName.text = trackUi?.trackName ?: ""
        binding.timer.text = trackUi?.trackTime ?: ""
        binding.releaseDate.text = trackUi?.releaseDate ?: ""
        binding.trackTime.text = trackUi?.trackTime ?: ""

        val trackImageUrl = trackUi?.artworkUrl100?.replaceAfterLast("/","512x512bb.jpg") ?: R.drawable.placeholder
        Glide.with(binding.trackImage)
            .load(trackImageUrl)
            .centerCrop()
            .transform(RoundedCorners(8.dpToPx(this)))
            .placeholder(R.drawable.placeholder)
            .into(binding.trackImage)

        binding.collectionNameContainer.visibility = if (trackUi?.collectionName.isNullOrEmpty()) View.GONE
        else {
            binding.collectionName.text = trackUi!!.collectionName
            View.VISIBLE
        }

        viewModel.observeState().observe(this) { state ->

            when(state) {
                is PlayerState.Prepared -> {
                    binding.playerIcon.isEnabled = true
                    binding.playerIcon.setImageResource(R.drawable.play_button)
                    binding.timer.text = state.currentTime
                }
                is PlayerState.Playing -> {
                    binding.playerIcon.setImageResource(R.drawable.pause_button)
                    binding.timer.text = state.currentTime
                }
                is PlayerState.Paused -> {
                    binding.playerIcon.setImageResource(R.drawable.play_button)
                    binding.timer.text = state.currentTime
                }
                is PlayerState.Default -> {
                    binding.timer.text = state.currentTime
                }
            }
        }

        viewModel.prepare(trackUrl)
        binding.playerIcon.isEnabled = false
        if (trackUrl.isNotEmpty()) {
            binding.playerIcon.setOnClickListener {
                viewModel.togglePlayback()
            }
        }
        else {
            binding.playerIcon.setOnClickListener {
                Toast.makeText (this, getString(R.string.notPreview), Toast.LENGTH_SHORT).show()
            }
        }



        binding.backButtonAudioplayer.setOnClickListener {
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val EXTRA_TRACK = "track"
    }
}