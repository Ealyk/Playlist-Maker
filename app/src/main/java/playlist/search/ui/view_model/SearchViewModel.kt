package playlist.search.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playlist.creator.Creator
import playlist.search.domain.TrackInteractor
import playlist.search.ui.model.TrackUi
import playlist.search.domain.model.TrackSearchState

class SearchViewModel: ViewModel() {

    private val searchingStateLiveData = MutableLiveData<TrackSearchState>()
    fun observeStateSearching(): LiveData<TrackSearchState> = searchingStateLiveData

    private val provideInteractor = Creator.provideTrackInteractor()
    private val searchHistory = Creator.provideHistoryInteractor()


    init {
        searchingStateLiveData.postValue(TrackSearchState.History(searchHistory.loadHistory()))
    }

    fun onTrackClicked(trackUi: TrackUi) {

        searchHistory.addTrackHistory(trackUi)
        searchingStateLiveData.postValue(TrackSearchState.History(searchHistory.loadHistory()))

    }

    fun loadTrack(query: String) {
        if (query.isNotBlank()) {
            searchingStateLiveData.postValue(TrackSearchState.Loading)
            provideInteractor.searchTracks(query, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: Result<List<TrackUi>>) {
                        foundTracks.onSuccess { tracks ->
                            if (tracks.isNotEmpty()) {
                                searchingStateLiveData.postValue(TrackSearchState.Content(tracks))
                            } else {
                                searchingStateLiveData.postValue(TrackSearchState.Empty())
                            }
                        }
                        foundTracks.onFailure {
                            searchingStateLiveData.postValue(TrackSearchState.Error())
                        }
                }

            })

        }
    }

    fun clearHistory() {
        searchHistory.clearHistory()
        searchingStateLiveData.postValue(TrackSearchState.History(emptyList()))
    }

    fun clearSearch() {
        searchingStateLiveData.value = TrackSearchState.History(searchHistory.loadHistory())

    }
}