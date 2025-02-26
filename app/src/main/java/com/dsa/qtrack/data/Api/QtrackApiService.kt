package com.dsa.qtrack.data.Api


import com.dsa.qtrack.data.model.Solicitud
import com.dsa.qtrack.data.model.QtrackResponse
import com.dsa.qtrack.model.LoginRequest
import com.dsa.qtrack.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface QtrackApiService {

    @GET("qtrackApi/getAll")
    suspend fun getSolicitudes(): QtrackResponse

    @POST("qtrackApi/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("qtrackApiController/getSolicitudesByMensajero/{id_mensajero}")
    suspend fun getSolicitudesByMensajero(
        @Path("id_mensajero") idMensajero: Int
    ): Response<QtrackResponse>







}
