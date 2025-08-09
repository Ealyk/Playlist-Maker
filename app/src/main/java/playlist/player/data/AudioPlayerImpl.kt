package playlist.player.data

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import playlist.player.domain.AudioPlayer
import playlist.player.domain.TimerUpdateListener
import java.text.SimpleDateFormat

class AudioPlayerImpl(
    private val mediaPlayer: MediaPlayer,
    private val format: SimpleDateFormat,
    private val context: Context,
    private val handler: Handler,
): AudioPlayer {

    private var updateRunnable: Runnable? = null
    private var timerUpdateListener: TimerUpdateListener? = null

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
                timerUpdateListener?.onTimerUpdate(format.format(mediaPlayer.currentPosition))
                handler.postDelayed(this, TIMER_TRACK_DELAY)
            }
        }.also { handler.post(it) }
    }
    override fun attachTimerListener(listener: TimerUpdateListener) {
        timerUpdateListener = listener
    }

    companion object {
        private const val TIMER_TRACK_DELAY = 400L
    }
}
