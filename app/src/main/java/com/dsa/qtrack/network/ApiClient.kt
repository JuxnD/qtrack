package com.dsa.qtrack.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Interceptor para mostrar logs de las peticiones (útil para debugging)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con el interceptor agregado
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Configuración de Gson para manejo flexible de JSON
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Instancia de Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.9:8080/control360i/") // Reemplaza con tu URL base si cambia
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // Servicio API listo para usar
    val apiService: QtrackApiService = retrofit.create(QtrackApiService::class.java)
}
