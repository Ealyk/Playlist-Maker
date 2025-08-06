package playlist.history.data

import android.content.SharedPreferences
import com.google.gson.Gson
import playlist.history.domain.HistoryRepository
import playlist.search.domain.model.Track




class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : HistoryRepository {



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