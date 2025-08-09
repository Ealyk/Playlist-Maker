package playlist.search.data.network

import playlist.search.data.NetworkClient
import playlist.search.data.dto.Response
import playlist.search.data.dto.TrackSearchRequest


class RetrofitNetworkClient(
    private val iTunesService: iTunesApi
) : NetworkClient {

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

}