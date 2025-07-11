package playlist.search.ui.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playlist.creator.Creator
import playlist.search.domain.TrackInteractor
import playlist.search.domain.model.Track
import playlist.search.domain.model.TrackSearchState

class SearchViewModel: ViewModel() {

    private val searchingStateLiveData = MutableLiveData<TrackSearchState>()
    fun observeStateSearching(): LiveData<TrackSearchState> = searchingStateLiveData

    private val trackListLiveData = MutableLiveData<List<Track>>()
    fun observeTrackList(): LiveData<List<Track>> = trackListLiveData

    private val historyLiveData = MutableLiveData<List<Track>>()
    fun observeHistory(): LiveData<List<Track>> = historyLiveData

    private val provideInteractor = Creator.provideTrackInteractor()
    private val searchHistory = Creator.provideHistoryInteractor()


    init {
        historyLiveData.value = searchHistory.loadHistory()
    }

    fun onTrackClicked(track: Track) {

        searchHistory.addTrackHistory(track)
        historyLiveData.value = searchHistory.loadHistory()

    }

    fun loadTrack(query: String) {
        if (query.isNotBlank()) {
            searchingStateLiveData.postValue(TrackSearchState.Loading)
            provideInteractor.searchTracks(query, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: Result<List<Track>>) {
                        foundTracks.onSuccess { tracks ->
                            if (tracks.isNotEmpty()) {
                                trackListLiveData.postValue(tracks)
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
        historyLiveData.value = emptyList()
    }

    fun clearSearch() {
        trackListLiveData.value = emptyList()
        searchingStateLiveData.value = if (searchHistory.loadHistory().isNotEmpty()) {
            TrackSearchState.History(searchHistory.loadHistory())
        } else TrackSearchState.Default

    }
}