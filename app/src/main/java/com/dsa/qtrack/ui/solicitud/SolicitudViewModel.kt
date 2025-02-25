package com.dsa.qtrack.ui.solicitud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.data.model.Solicitud
import com.dsa.qtrack.api.ApiClient
import com.dsa.qtrack.data.Api.QtrackApiService
import kotlinx.coroutines.launch

class SolicitudViewModel : ViewModel() {

    private val _solicitudes = MutableLiveData<List<Solicitud>>()
    val solicitudes: LiveData<List<Solicitud>> get() = _solicitudes

    // 🔍 Obtiene todas las solicitudes
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

    // 🚀 Obtiene las solicitudes asignadas al mensajero en estado "abierto"
    fun fetchSolicitudesByMensajero(idMensajero: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.retrofit.create(QtrackApiService::class.java).getSolicitudesByMensajero(idMensajero)
                if (response.isSuccessful) {
                    val solicitudesFiltradas = response.body()?.data?.rows?.filter {
                        it.estatus.equals("abierto", ignoreCase = true)
                    } ?: emptyList()

                    _solicitudes.value = solicitudesFiltradas
                } else {
                    println("⚠️ Error al obtener solicitudes: Código ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
