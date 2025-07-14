package playlist.search.data

import android.os.Handler
import android.os.Looper
import playlist.search.domain.TrackInteractor
import playlist.search.domain.TrackRepository
import playlist.search.ui.TrackMapper

class TrackInteractorImpl(
    private val repository: TrackRepository
) : TrackInteractor {

    override fun searchTracks(term: String, consumer: TrackInteractor.TrackConsumer) {

        val thread = Thread {
            val result = repository.searchTrack(term)
                .map { tracks -> tracks.map { TrackMapper.toUi(it) } }

            Handler(Looper.getMainLooper()).post {
                consumer.consume(result)
            }
        }
        thread.start()
    }

}