package com.dsa.qtrack.data.Api

import com.dsa.qtrack.data.model.LoginRequest
import com.dsa.qtrack.data.model.LoginResponse
import com.dsa.qtrack.data.model.QtrackResponse
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
