package playlist.history.domain.impl

import playlist.history.domain.HistoryInteractor
import playlist.history.domain.HistoryRepository
import playlist.search.ui.model.TrackUi

class HistoryInteractorImpl(private val repository: HistoryRepository): HistoryInteractor  {



    override fun loadHistory(): List<TrackUi> {
        return repository.loadHistory()
    }

    override fun addTrackHistory(trackUi: TrackUi) {

        val historyList = repository.loadHistory().toMutableList()
        historyList.remove(trackUi)

        if (historyList.size >= 10) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, trackUi)
        repository.saveHistory(historyList)

    }

    override fun clearHistory() {
        repository.saveHistory(emptyList())
    }

}