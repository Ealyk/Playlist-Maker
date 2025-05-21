package ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.ealyk.playlistmaker2.Creator
import com.ealyk.playlistmaker2.R
import domain.api.AudioPlayer
import domain.models.Track



class AudioplayerActivity : AppCompatActivity() {

    private var playerState = STATE_DEFAULT

    private lateinit var provideAudioPlayer: AudioPlayer

    private lateinit var handler: Handler

    private lateinit var trackUrl: String

    private lateinit var backButton: Button
    private lateinit var collectionNameContainer: ConstraintLayout
    private lateinit var collectionName: TextView
    private lateinit var country: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var timer: TextView
    private lateinit var trackTime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var trackImage: ImageView
    private lateinit var playButton: ImageView

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

        val track =  intent.getParcelableExtra<Track>(EXTRA_TRACK)
        trackUrl = track?.previewUrl ?: ""

        handler = Handler(Looper.getMainLooper())

        backButton = findViewById(R.id.back_buttonAudioplayer)
        collectionNameContainer = findViewById(R.id.collectionNameContainer)
        collectionName = findViewById(R.id.collectionName)
        country = findViewById(R.id.country)
        primaryGenreName = findViewById(R.id.genre)
        artistName = findViewById(R.id.nameGroup)
        trackName = findViewById(R.id.trackName)
        timer = findViewById(R.id.timer)
        releaseDate = findViewById(R.id.releaseDate)
        trackTime = findViewById(R.id.trackTime)
        trackImage = findViewById(R.id.trackImage)
        playButton = findViewById(R.id.playerIcon)

        country.text = track?.country ?: ""
        primaryGenreName.text = track?.primaryGenreName ?: ""
        artistName.text = track?.artistName ?: ""
        trackName.text = track?.trackName ?: ""
        timer.text = track?.trackTime ?: ""
        releaseDate.text = track?.releaseDate ?: ""
        trackTime.text = track?.trackTime ?: ""

        val trackImageUrl = track?.artworkUrl100?.replaceAfterLast("/","512x512bb.jpg") ?: R.drawable.placeholder
        Glide.with(trackImage)
            .load(trackImageUrl)
            .centerCrop()
            .transform(RoundedCorners(8.dpToPx(this)))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)

        collectionNameContainer.visibility = if (track?.collectionName.isNullOrEmpty()) View.GONE
        else {
            collectionName.text = track!!.collectionName
            View.VISIBLE
        }
        provideAudioPlayer = Creator.provideAudioPlayer(handler) { formatedTime ->
            timer.text = formatedTime

        }

        provideAudioPlayer.prepare(
            url = trackUrl,
            onPrepared = {
                playButton.isEnabled = true
                playerState = STATE_PREPARED
            },
            onCompleted = {
                playButton.setImageResource(R.drawable.play_button)
                timer.text = START_TIMER_VALUE
            }
        )
        if (trackUrl.isNotEmpty()) {
            playButton.setOnClickListener {
                playbackControl()
            }
        }
        else {
            playButton.setOnClickListener {
                Toast.makeText (this, getString(R.string.notPreview), Toast.LENGTH_SHORT).show()
            }
        }



        backButton.setOnClickListener {
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        playButton.setImageResource(R.drawable.play_button)
        provideAudioPlayer.pausePlayer()
        playerState = STATE_PAUSED
    }

    override fun onDestroy() {
        super.onDestroy()
        provideAudioPlayer.release()
        timer.text = START_TIMER_VALUE
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                provideAudioPlayer.pausePlayer()
                playerState = STATE_PAUSED
                playButton.setImageResource(R.drawable.play_button)
            }
            STATE_PAUSED, STATE_PREPARED -> {

                provideAudioPlayer.startPlayer()
                playerState = STATE_PLAYING
                playButton.setImageResource(R.drawable.pause_button)
            }
        }
    }




    companion object {
        private const val EXTRA_TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val START_TIMER_VALUE = "00:00"
    }

}