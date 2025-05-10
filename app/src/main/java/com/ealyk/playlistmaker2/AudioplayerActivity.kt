package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
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
import java.text.SimpleDateFormat
import java.util.Locale


class AudioplayerActivity : AppCompatActivity() {

    private var playerState = STATE_DEFAULT

    private val mediaPlayer = MediaPlayer()

    private val dataFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private lateinit var handler: Handler

    private lateinit var trackUrl: String

    private val updateRunnable = updateTimer()

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
        timer.text = track?.formatedTime ?: ""
        releaseDate.text = track?.formatRelease() ?: ""
        trackTime.text = track?.formatedTime ?: ""

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

        preparePlayer()
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
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateRunnable)
        timer.text = START_TIMER_VALUE
    }

    private fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun preparePlayer() {
        mediaPlayer.apply {
            setDataSource(trackUrl)
            prepareAsync()
            setOnPreparedListener {
                playButton.isEnabled = true
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playButton.setImageResource(R.drawable.play_button)
                handler.removeCallbacks(updateRunnable)
                timer.text = START_TIMER_VALUE
            }
        }
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PAUSED, STATE_PREPARED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateRunnable)
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(updateRunnable)
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }
    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                timer.text = dataFormat.format(mediaPlayer.currentPosition)
                handler.postDelayed(this, TIMER_TRACK_DELAY)
            }

        }
    }

    companion object {
        private const val EXTRA_TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIMER_TRACK_DELAY = 400L
        private const val START_TIMER_VALUE = "00:00"
    }

}