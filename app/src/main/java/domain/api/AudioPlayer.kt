package domain.api

interface AudioPlayer {

    fun prepare(
        url: String,
        onPrepared: () -> Unit,
        onCompleted: () -> Unit
    )
    fun startPlayer()
    fun pausePlayer()
    fun getCurrentPosition(): Int
    fun release()

}