package playlist.history.domain


import playlist.search.ui.model.TrackUi

interface HistoryInteractor {
    fun loadHistory(): List<TrackUi>
    fun addTrackHistory(trackUi: TrackUi)
    fun clearHistory()
}