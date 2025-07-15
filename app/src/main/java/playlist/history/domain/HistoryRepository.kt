package playlist.history.domain
import playlist.search.domain.model.Track

interface HistoryRepository {

    fun loadHistory(): List<Track>

    fun saveHistory(historyList: List<Track>)
}