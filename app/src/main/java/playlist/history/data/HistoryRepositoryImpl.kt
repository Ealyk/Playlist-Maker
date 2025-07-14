package playlist.history.data

import android.content.SharedPreferences
import com.google.gson.Gson
import playlist.history.domain.HistoryRepository
import playlist.search.ui.model.TrackUi



class HistoryRepositoryImpl(private val sharedPreferences: SharedPreferences): HistoryRepository {

    private val gson = Gson()

    override fun loadHistory(): List<TrackUi> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return listOf()
        return gson.fromJson(json, Array<TrackUi>::class.java).toList()
    }

    override fun saveHistory(historyList: List<TrackUi>) {

        val json = gson.toJson(historyList)
        sharedPreferences.edit()
            .putString(KEY_HISTORY, json)
            .apply()

    }


    companion object {
        private const val KEY_HISTORY = "Key history"
    }

}