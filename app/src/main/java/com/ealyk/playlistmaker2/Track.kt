package com.ealyk.playlistmaker2

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?
): Parcelable {
    val formatedTime: String
        get() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)



    fun formatRelease(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(inputFormat.parse(releaseDate)!!)
    }

}

