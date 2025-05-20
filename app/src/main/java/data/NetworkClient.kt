package data

import data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}