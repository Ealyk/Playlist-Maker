package playlist.player.data

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import playlist.player.domain.AudioPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerImpl(
    private val context: Context,
    private val handler: Handler,
    private val updateCallback: (String) -> Unit
): AudioPlayer {

    private val mediaPlayer = MediaPlayer()
    private var updateRunnable: Runnable? = null
    private val format = SimpleDateFormat("mm:ss", Locale.getDefault())

    override fun prepare(url: String, onPrepared: () -> Unit, onCompleted: () -> Unit) {
            mediaPlayer.setDataSource(context, Uri.parse(url))
            mediaPlayer.setOnPreparedListener {
                onPrepared()
            }
            mediaPlayer.setOnCompletionListener {
                stopUpdating()
                onCompleted()
            }
            mediaPlayer.prepareAsync()

    }

    override fun startPlayer() {
        mediaPlayer.start()
        startUpdating()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        stopUpdating()
    }

    override fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    override fun release() {
        stopUpdating()
        mediaPlayer.release()
    }

    private fun stopUpdating() {
        updateRunnable?.let { handler.removeCallbacks(it) }
    }

    private fun startUpdating() {
        updateRunnable = object : Runnable {
            override fun run() {
                updateCallback(format.format(mediaPlayer.currentPosition))
                handler.postDelayed(this, TIMER_TRACK_DELAY)
            }
        }.also { handler.post(it) }
    }

    companion object {
        private const val TIMER_TRACK_DELAY = 400L
    }
}
