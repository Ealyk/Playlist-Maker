package playlist.history.domain
import playlist.search.ui.model.TrackUi

interface HistoryRepository {

    fun loadHistory(): List<TrackUi>

    fun saveHistory(historyList: List<TrackUi>)
}