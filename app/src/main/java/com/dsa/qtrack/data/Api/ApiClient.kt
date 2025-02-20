package com.dsa.qtrack.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val gson = GsonBuilder().setLenient().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.9:8080/control360i/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}