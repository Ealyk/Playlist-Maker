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

    private val playerStateLiveData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observeState(): LiveData<PlayerState> = playerStateLiveData


    private val audioPlayer = Creator.provideAudioPlayer(handler) { formattedTime ->
        val currentState = playerStateLiveData.value
        when(currentState) {
            is PlayerState.Playing -> playerStateLiveData.postValue(PlayerState.Playing(formattedTime))
            is PlayerState.Paused -> playerStateLiveData.postValue(PlayerState.Paused(formattedTime))
            else -> Unit
        }
    }


    fun prepare(url: String) {
        audioPlayer.prepare(
            url = url,
            onPrepared = {
                playerStateLiveData.value = PlayerState.Prepared()
            },
            onCompleted = {
                playerStateLiveData.value = PlayerState.Prepared()
            }
        )
    }

    fun play() {
        audioPlayer.startPlayer()
        val lastTime = when (val state = playerStateLiveData.value) {
            is PlayerState.Paused -> state.currentTime
            is PlayerState.Prepared -> state.currentTime
            else -> START_TIMER_VALUE
        }
        playerStateLiveData.postValue(PlayerState.Playing(lastTime))
    }


    fun pause() {
        audioPlayer.pausePlayer()
        val currentTime = (playerStateLiveData.value as? PlayerState.Playing)?.currentTime ?: START_TIMER_VALUE
        playerStateLiveData.value = PlayerState.Paused(currentTime)
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


    companion object {
        private const val START_TIMER_VALUE = "00:00"
    }

}