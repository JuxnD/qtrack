package com.dsa.qtrack.network

import com.dsa.qtrack.model.QtrackResponse
import retrofit2.http.GET

interface QtrackApiService {
    @GET("qtrackApi/getAll")
    suspend fun getSolicitudes(): QtrackResponse
}
