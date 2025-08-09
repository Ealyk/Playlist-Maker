package playlist.search.domain

import playlist.search.domain.model.Track

interface TrackInteractor {

    fun searchTracks(term: String, consumer: TrackConsumer)


    interface TrackConsumer {
        fun consume(foundTracks: Result<List<Track>>)
    }
}