package domain.impl

import domain.api.HistoryInteractor
import domain.api.HistoryObserver
import domain.api.HistoryRepository
import domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository): HistoryInteractor {

    private var observers = mutableListOf<HistoryObserver>()

    override fun loadHistory(): List<Track> {
        return repository.loadHistory()
    }

    override fun addTrackHistory(track: Track) {

        val historyList = repository.loadHistory().toMutableList()
        historyList.remove(track)

        if (historyList.size >= 10) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)
        repository.saveHistory(historyList)

    }

    override fun clearHistory() {
        repository.saveHistory(emptyList())
    }

    override fun addObserver(observer: HistoryObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: HistoryObserver) {
        observers.remove(observer)
    }
}