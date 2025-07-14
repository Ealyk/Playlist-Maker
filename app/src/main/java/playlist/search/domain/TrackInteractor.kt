package playlist.search.domain

import playlist.search.ui.model.TrackUi

interface TrackInteractor {

    fun searchTracks(term: String, consumer: TrackConsumer)


    interface TrackConsumer {
        fun consume(foundTracks: Result<List<TrackUi>>)
    }
}