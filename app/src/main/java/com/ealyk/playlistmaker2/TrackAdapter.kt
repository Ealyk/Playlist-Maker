package com.ealyk.playlistmaker2

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val trackList: List<Track>,
    private val searchHistory: SearchHistory
): RecyclerView.Adapter<TrackViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            val track = trackList[position]
            searchHistory.addTrackHistory(track)

        }
    }


}