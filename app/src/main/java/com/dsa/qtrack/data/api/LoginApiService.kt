package com.dsa.qtrack.data.api

import android.util.Log
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApiService {
    @POST("qtrackApi/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

// Agrega un log en la implementación para verificar la solicitud
suspend fun LoginApiService.safeLogin(request: LoginRequest): Response<LoginResponse> {
    return try {
        Log.d("LoginDebug", "Enviando login: Email=${request.email}, Password=${request.password}")
        val response = login(request)
        Log.d("LoginDebug", "Respuesta recibida: Código=${response.code()}, Body=${response.body()}")
        response
    } catch (e: Exception) {
        Log.e("LoginDebug", "Error en la solicitud de login: ${e.localizedMessage}", e)
        throw e
    }
}
