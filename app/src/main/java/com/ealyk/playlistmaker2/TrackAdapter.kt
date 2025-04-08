package com.ealyk.playlistmaker2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ealyk.playlistmaker2.Constants.EXTRA_TRACK
import com.google.gson.Gson

class TrackAdapter(
    private val trackList: MutableList<Track>,
    private val sharedPreferences: SharedPreferences,
    private val isHistory: Boolean = false,
    private val context: Context
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
            val intent = Intent(context, AudioplayerActivity::class.java)
            val track = trackList[position]

            intent.putExtra(EXTRA_TRACK, track)
            if (!isHistory) {
                SearchHistory(sharedPreferences).addTrackHistory(track)
            }
            context.startActivity(intent)

        }
    }

    fun updateList(newList: List<Track>) {
        trackList.clear()
        trackList.addAll(newList)
        notifyDataSetChanged()
    }

}