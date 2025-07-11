package playlist.search.data.network

import playlist.search.data.NetworkClient
import playlist.search.data.dto.Response
import playlist.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesbaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)


    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = iTunesService.search(dto.term).execute()
            val body = resp.body() ?: Response()
            return body.apply { resultCode = resp.code() }
        }
        else {
            return Response().apply { resultCode = 400 }
        }
    }

    companion object {
        private const val iTunesbaseUrl = "https://itunes.apple.com"
    }
}