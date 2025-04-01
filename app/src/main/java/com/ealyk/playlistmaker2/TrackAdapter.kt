package com.ealyk.playlistmaker2

import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val trackList: MutableList<Track>,
    private val sharedPreferences: SharedPreferences,
    private val isHistory: Boolean = false
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
            if (!isHistory) {
                SearchHistory(sharedPreferences).addTrackHistory(track)
            }

        }
    }

    fun updateList(newList: List<Track>) {
        trackList.clear()
        trackList.addAll(newList)
        notifyDataSetChanged()
    }

}