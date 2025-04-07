package com.ealyk.playlistmaker2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var collectionNameContainer: FrameLayout
    private lateinit var collectionName: TextView
    private lateinit var country: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var timer: TextView
    private lateinit var trackTime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var trackImage: ImageView

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

        val trackJson = intent.getStringExtra("track")
        val track =  Gson().fromJson(trackJson, Track::class.java)


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

        country.text = track.country
        primaryGenreName.text = track.primaryGenreName
        artistName.text = track.artistName
        trackName.text = track.trackName
        timer.text = track.formatedTime
        releaseDate.text = track.formatRelease()
        trackTime.text = track.formatedTime

        val trackImageUrl = track.artworkUrl100.replaceAfterLast("/","512x512bb.jpg")
        Glide.with(trackImage)
            .load(trackImageUrl)
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.placeholder)
            .into(trackImage)

        collectionNameContainer.visibility = if (track.collectionName.isEmpty()) View.GONE
        else {
            collectionName.text = track.collectionName
            View.VISIBLE
        }

        backButton.setOnClickListener {
            finish()
        }



    }
}