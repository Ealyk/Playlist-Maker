package playlist.search.domain.model

import playlist.search.ui.model.TrackUi

sealed interface TrackSearchState {

    object Loading: TrackSearchState
    data class Content(val data: List<TrackUi>): TrackSearchState
    class Error: TrackSearchState
    class Empty: TrackSearchState
    data class History(val history: List<TrackUi>): TrackSearchState

}