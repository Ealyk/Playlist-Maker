package data.network

import data.dto.TrackSearchResponse
import domain.models.Track
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface iTunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackSearchResponse>

}