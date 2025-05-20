package domain.api

import domain.models.Track

interface TrackRepository {
    fun searchTrack(term: String): Result<List<Track>>

}