package com.dsa.qtrack.ui.solicitud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsa.qtrack.data.api.ApiClient
import com.dsa.qtrack.data.model.Solicitud
import kotlinx.coroutines.launch

class SolicitudViewModel : ViewModel() {

    private val _solicitudesAbiertas = MutableLiveData<List<Solicitud>>()
    val solicitudesAbiertas: LiveData<List<Solicitud>> get() = _solicitudesAbiertas

    init {
        fetchSolicitudesAbiertas()  // Cambiado de obtenerSolicitudesAbiertas a fetchSolicitudesAbiertas
    }

    fun fetchSolicitudesAbiertas() {
        viewModelScope.launch {
            try {
                val response = ApiClient.qtrackApiService.getSolicitudesByMensajero(idMensajero = 164)
                if (response.isSuccessful) {
                    val solicitudesFiltradas = response.body()?.data?.rows?.filter { it.estatus == "abierto" } ?: emptyList()
                    _solicitudesAbiertas.postValue(solicitudesFiltradas)
                } else {
                    _solicitudesAbiertas.postValue(emptyList())  // Manejo de error
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _solicitudesAbiertas.postValue(emptyList())
            }
        }
    }
}
