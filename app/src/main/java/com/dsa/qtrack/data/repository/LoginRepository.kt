package com.dsa.qtrack.repository

import com.dsa.qtrack.api.ApiClient
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import retrofit2.Response

class LoginRepository {
    private val api = ApiClient.loginApiService

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return api.login(LoginRequest(email, password))
    }
}
