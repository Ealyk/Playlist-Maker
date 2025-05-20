package data

import android.content.Context
import com.google.gson.Gson
import domain.api.HistoryObserver
import domain.api.HistoryRepository
import domain.models.Track



class HistoryRepositoryImpl(context: Context): HistoryRepository {

    private val sharedPreferences = context.getSharedPreferences(KEY_HISTORY, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun loadHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return listOf()
        return gson.fromJson(json, Array<Track>::class.java).toList()
    }

    override fun saveHistory(historyList: List<Track>) {

        val json = gson.toJson(historyList)
        sharedPreferences.edit()
            .putString(KEY_HISTORY, json)
            .apply()

    }


    companion object {
        private const val KEY_HISTORY = "Key history"
    }

}