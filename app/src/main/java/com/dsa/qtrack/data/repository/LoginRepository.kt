package com.dsa.qtrack.data.repository

import com.dsa.qtrack.data.api.ApiClient
import com.dsa.qtrack.data.api.LoginApiService
import com.dsa.qtrack.data.model.LoginRequest
import com.dsa.qtrack.data.model.LoginResponse
import retrofit2.Response

class LoginRepository {
    private val api = ApiClient.retrofit.create(LoginApiService::class.java)

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }
}