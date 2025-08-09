package playlist.search.domain

import android.os.Handler
import android.os.Looper

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TrackInteractor {

    override fun searchTracks(term: String, consumer: TrackInteractor.TrackConsumer) {

        val thread = Thread {
            val result = repository.searchTrack(term)

            Handler(Looper.getMainLooper()).post {
                consumer.consume(result)
            }
        }
        thread.start()
    }

}