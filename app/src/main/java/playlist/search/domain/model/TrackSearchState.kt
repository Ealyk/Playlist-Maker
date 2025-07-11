package playlist.search.domain.model

sealed interface TrackSearchState {

    object Loading: TrackSearchState
    data class Content(val data: List<Track>): TrackSearchState
    class Error: TrackSearchState
    class Empty: TrackSearchState
    data class History(val history: List<Track>): TrackSearchState
    object Default: TrackSearchState

}