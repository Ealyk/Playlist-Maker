package playlist.player.ui.view_model


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import playlist.creator.Creator
import playlist.player.domain.model.PlayerState

class AudioplayerViewModel: ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default)
    fun observeState(): LiveData<PlayerState> = playerStateLiveData

    private val timerLiveData = MutableLiveData(START_TIMER_VALUE)
    fun observeTimer(): LiveData<String> = timerLiveData

    private val audioPlayer = Creator.provideAudioPlayer(handler) { formatedTime ->
        timerLiveData.postValue(formatedTime)
    }


    fun prepare(url: String) {
        audioPlayer.prepare(
            url = url,
            onPrepared = {
                playerStateLiveData.value = PlayerState.Prepared
            },
            onCompleted = {
                timerLiveData.value = START_TIMER_VALUE
                playerStateLiveData.value = PlayerState.Prepared
            }
        )
    }

    fun play() {
        audioPlayer.startPlayer()
        playerStateLiveData.value = PlayerState.Playing
    }

    fun pause() {
        audioPlayer.pausePlayer()
        playerStateLiveData.value = PlayerState.Paused
    }

    fun release() {
        audioPlayer.release()
    }

    fun togglePlayback() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pause()
            is PlayerState.Prepared, is PlayerState.Paused -> play()
            else -> Unit
        }
    }

    fun updateTimer(time: String) {
        timerLiveData.value = time
    }


    companion object {
        private const val START_TIMER_VALUE = "00:00"

    }
}