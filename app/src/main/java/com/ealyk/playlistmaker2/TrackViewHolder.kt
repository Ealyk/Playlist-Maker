package com.ealyk.playlistmaker2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false)
) {

    private val trackName = itemView.findViewById<TextView>(R.id.trackCardName)
    private val artistName = itemView.findViewById<TextView>(R.id.trackCardNameArtist)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackCardTime)
    private val artWorkUrl = itemView.findViewById<ImageView>(R.id.trackCardImage)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(artWorkUrl)

    }
}