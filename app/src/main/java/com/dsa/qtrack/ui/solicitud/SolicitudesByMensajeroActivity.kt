package com.dsa.qtrack.ui.solicitud

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R
import com.dsa.qtrack.SolicitudAdapter
import com.dsa.qtrack.session.SessionManager

class SolicitudesByMensajeroActivity : AppCompatActivity() {

    private lateinit var solicitudViewModel: SolicitudViewModel
    private lateinit var solicitudAdapter: SolicitudAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_by_mensajero)

        // Inicializa el SessionManager y obtiene el ID del usuario
        sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId()

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        solicitudAdapter = SolicitudAdapter(emptyList())
        recyclerView.adapter = solicitudAdapter

        // Inicializa el ViewModel
        solicitudViewModel = ViewModelProvider(this)[SolicitudViewModel::class.java]

        // Observa las solicitudes y filtra las asignadas al mensajero con estado "abierto"
        solicitudViewModel.solicitudes.observe(this) { solicitudes ->
            val filteredSolicitudes = solicitudes.filter {
                it.id_mensajero == userId && it.estatus.equals("abierto", ignoreCase = true)
            }

            Log.d("SolicitudesByMensajero", "Solicitudes abiertas asignadas: ${filteredSolicitudes.size}")
            solicitudAdapter.updateSolicitudes(filteredSolicitudes)
        }

        // Llama al método para obtener las solicitudes filtradas por mensajero
        Log.d("SolicitudesByMensajero", "Solicitando solicitudes por mensajero.")
        solicitudViewModel.fetchSolicitudesByMensajero(userId)
    }
}
