package com.dsa.qtrack.network

import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import com.dsa.qtrack.model.QtrackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface QtrackApiService {

    @GET("qtrackApi/getAll")
    suspend fun getSolicitudes(): QtrackResponse

    @POST("qtrackApi/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
