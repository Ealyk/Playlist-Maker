package data

import data.dto.TrackSearchRequest
import data.dto.TrackSearchResponse
import domain.api.TrackRepository
import domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {


    override fun searchTrack(term: String): Result<List<Track>> {
        return try {
            val response = networkClient.doRequest(TrackSearchRequest(term))
            if (response.resultCode == 200) {
                val tracks = (response as TrackSearchResponse).results.map {
                    Track(
                        it.trackName,
                        it.artistName,
                        timeFromMillis(it.trackTime),
                        it.artworkUrl100,
                        it.collectionName,
                        formatReleaseDate(it.releaseDate),
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                }
                Result.success(tracks)
            } else return Result.failure(Exception("${response.resultCode}"))
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun timeFromMillis(trackTimeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
    private fun formatReleaseDate(releaseDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(inputFormat.parse(releaseDate)!!)
    }

}