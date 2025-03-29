package com.ealyk.playlistmaker2

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(val historyList: MutableList<Track>): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {

        holder.bind(historyList[position])
    }

    fun updateList(newList: List<Track>) {
        historyList.clear()
        historyList.addAll(newList)
        notifyDataSetChanged()
    }
}