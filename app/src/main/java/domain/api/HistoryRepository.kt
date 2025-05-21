package domain.api
import domain.models.Track

interface HistoryRepository {

    fun loadHistory(): List<Track>

    fun saveHistory(historyList: List<Track>)
}