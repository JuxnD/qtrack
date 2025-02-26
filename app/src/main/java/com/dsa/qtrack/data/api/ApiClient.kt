package com.dsa.qtrack.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val gson = GsonBuilder().setLenient().create()

    private var _retrofit: Retrofit? = null

    val retrofit: Retrofit
        get() {
            if (_retrofit == null) {
                _retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.20.51:8080/control360i/") // Cambia la URL si es necesario
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return _retrofit!!
        }

    val loginApiService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }

    val qtrackApiService: QtrackApiService by lazy {
        retrofit.create(QtrackApiService::class.java)
    }

    fun resetClient() {
        _retrofit = null
    }
}
