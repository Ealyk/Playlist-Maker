package com.ealyk.playlistmaker2

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,

) {
    val formatedTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
}

