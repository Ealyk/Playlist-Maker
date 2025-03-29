package com.ealyk.playlistmaker2

import android.app.Application
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.database.Observable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface HistoryObserver {
    fun onHistoryUpdate(historyList: List<Track>)
}

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private var observers = mutableListOf<HistoryObserver>()

    fun loadHistory(): Array<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return arrayOf()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    private fun saveHistory(historyList: List<Track>) {

        val json = Gson().toJson(historyList)
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

    private fun notifyObservers(historyList: List<Track>) {
        observers.forEach { it.onHistoryUpdate(historyList) }
    }



    companion object {
        const val KEY_HISTORY = "Key history"

    }

}