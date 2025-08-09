package playlist.search.domain

import playlist.search.domain.model.Track


interface TrackRepository {
    fun searchTrack(term: String): Result<List<Track>>

}