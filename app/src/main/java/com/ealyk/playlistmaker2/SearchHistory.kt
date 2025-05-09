package com.ealyk.playlistmaker2

import android.content.SharedPreferences
import com.google.gson.Gson


interface HistoryObserver {
    fun onHistoryUpdate(historyList: List<Track>)
}

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_HISTORY = "Key history"
    }

    private var observers = mutableListOf<HistoryObserver>()
    private val gson = Gson()

    fun loadHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return listOf()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }

    private fun saveHistory(historyList: List<Track>) {

        val json = gson.toJson(historyList)
        sharedPreferences.edit()
            .putString(KEY_HISTORY, json)
            .apply()

    }

    fun addTrackHistory(track: Track) {

        val historyList = loadHistory().toMutableList()
        historyList.remove(track)

        if (historyList.size >= 10) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)
        saveHistory(historyList)

    }

    fun clearHistory() {
        saveHistory(emptyList())
    }

    fun addObserver(observer: HistoryObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: HistoryObserver) {
        observers.remove(observer)
    }



}