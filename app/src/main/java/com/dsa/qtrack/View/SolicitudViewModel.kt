package com.dsa.qtrack.View

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.Solicitud
import com.dsa.qtrack.network.ApiClient
import com.dsa.qtrack.network.QtrackApiService
import kotlinx.coroutines.launch

class SolicitudViewModel : ViewModel() {
    private val _solicitudes = MutableLiveData<List<Solicitud>>()
    val solicitudes: LiveData<List<Solicitud>> get() = _solicitudes

    fun fetchSolicitudes() {
        viewModelScope.launch {
            try {
                val response = ApiClient.retrofit.create(QtrackApiService::class.java).getSolicitudes()
                _solicitudes.value = response.data?.rows ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}