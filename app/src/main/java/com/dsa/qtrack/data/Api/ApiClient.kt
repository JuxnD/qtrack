package com.dsa.qtrack.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val gson = GsonBuilder().setLenient().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.9:8080/control360i/") // Cambia la URL si es necesario
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val loginApiService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}
