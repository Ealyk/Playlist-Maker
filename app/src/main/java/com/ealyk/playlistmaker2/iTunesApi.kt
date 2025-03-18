package com.ealyk.playlistmaker2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

class TrackResponse(val resultCount: Int, val results: ArrayList<Track>) {

}
interface iTunesApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TrackResponse>

}