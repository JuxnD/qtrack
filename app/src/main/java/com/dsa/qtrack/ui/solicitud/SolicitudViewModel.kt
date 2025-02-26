package com.dsa.qtrack.viewmodel

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

    /**
     * Método para obtener las solicitudes abiertas desde la API.
     */
    fun obtenerSolicitudesAbiertas() {
        viewModelScope.launch {
            try {
                val response = ApiClient.qtrackApiService.getSolicitudesByMensajero(idMensajero = 164)
                if (response.isSuccessful) {
                    val solicitudesFiltradas = response.body()?.data?.rows?.filter { it.estatus?.equals("abierto", ignoreCase = true) == true } ?: emptyList()
                    _solicitudesAbiertas.postValue(solicitudesFiltradas)
                } else {
                    // Manejo de error si la respuesta no es exitosa
                    _solicitudesAbiertas.postValue(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _solicitudesAbiertas.postValue(emptyList())
            }
        }
    }

    /**
     * Método para exponer las solicitudes abiertas como LiveData.
     */
    fun getSolicitudesAbiertas(): LiveData<List<Solicitud>> = solicitudesAbiertas
}
