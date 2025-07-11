package playlist.player.domain.model

sealed interface PlayerState {
    object Prepared: PlayerState
    object Default: PlayerState
    object Playing: PlayerState
    object Paused: PlayerState
}