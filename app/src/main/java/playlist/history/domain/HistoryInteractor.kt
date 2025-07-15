package playlist.history.domain


import playlist.search.domain.model.Track

interface HistoryInteractor {
    fun loadHistory(): List<Track>
    fun addTrackHistory(track: Track)
    fun clearHistory()
}