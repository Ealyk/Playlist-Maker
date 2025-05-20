package domain.api

import domain.models.Track

interface HistoryObserver {
    fun onHistoryUpdate(historyList: List<Track>)
}