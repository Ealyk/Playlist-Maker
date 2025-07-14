package playlist.player.domain.model

sealed class PlayerState(open val currentTime: String) {
    data class Prepared(override val currentTime: String = START_TIMER_VALUE): PlayerState(currentTime)
    data class Default(override val currentTime: String = START_TIMER_VALUE) : PlayerState(currentTime)
    data class Playing(override val currentTime: String) : PlayerState(currentTime)
    data class Paused(override val currentTime: String) : PlayerState(currentTime)

    companion object {
        private const val START_TIMER_VALUE = "00:00"
    }
}