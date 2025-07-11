package playlist.search.data

import playlist.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}