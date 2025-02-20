package com.dsa.qtrack.data.api

import com.dsa.qtrack.data.model.LoginRequest
import com.dsa.qtrack.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("qtrackApi/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}