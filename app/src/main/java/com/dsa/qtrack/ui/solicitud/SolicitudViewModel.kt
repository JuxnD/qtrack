package com.dsa.qtrack.ui.solicitud

import android.content.Context
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


    fun fetchSolicitudesAbiertas(context: Context) {
        val sharedPreferences = context.getSharedPreferences("QTrackPrefs", Context.MODE_PRIVATE)
        val idMensajero = sharedPreferences.getInt("USER_ID", -1) // 🛡️ Reemplaza 164 por valor dinámico

        if (idMensajero == -1) {
            _solicitudesAbiertas.postValue(emptyList()) // Evita llamadas sin usuario válido
            return
        }

        viewModelScope.launch {
            try {
                val response = ApiClient.qtrackApiService.getSolicitudesByMensajero(idMensajero)
                if (response.isSuccessful) {
                    val solicitudesFiltradas = response.body()?.data?.rows?.filter { it.estatus == "abierto" } ?: emptyList()
                    _solicitudesAbiertas.postValue(solicitudesFiltradas)
                } else {
                    _solicitudesAbiertas.postValue(emptyList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _solicitudesAbiertas.postValue(emptyList())
            }
        }
    }
}
