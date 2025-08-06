package playlist.player.domain

interface TimerUpdateListener {
    fun onTimerUpdate(formatedTime: String)
}