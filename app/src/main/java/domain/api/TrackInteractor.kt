package domain.api

import domain.models.Track

interface TrackInteractor {

    fun searchTracks(term: String, consumer: TrackConsumer)


    interface TrackConsumer {
        fun consume(foundTracks: Result<List<Track>>)
    }
}