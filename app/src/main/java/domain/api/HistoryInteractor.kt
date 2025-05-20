package domain.api

import domain.models.Track

interface HistoryInteractor {
    fun loadHistory(): List<Track>
    fun addTrackHistory(track: Track)
    fun clearHistory()
    fun addObserver(observer: HistoryObserver)
    fun removeObserver(observer: HistoryObserver)
}