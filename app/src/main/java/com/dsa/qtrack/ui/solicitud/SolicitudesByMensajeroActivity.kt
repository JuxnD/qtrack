package com.dsa.qtrack.ui.solicitud

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R


class SolicitudesByMensajeroActivity : AppCompatActivity() {

    private val solicitudViewModel: SolicitudViewModel by viewModels()
    private lateinit var solicitudAdapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_by_mensajero)

        setupRecyclerView()
        fetchSolicitudes()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSolicitudes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        solicitudAdapter = SolicitudAdapter(emptyList())
        recyclerView.adapter = solicitudAdapter

        solicitudViewModel.solicitudesAbiertas.observe(this) { solicitudes ->
            solicitudAdapter.actualizarLista(solicitudes)
        }
    }

    private fun fetchSolicitudes() {
        solicitudViewModel.fetchSolicitudesAbiertas(this) // Se pasa el contexto para obtener el ID del mensajero desde SharedPreferences
    }
}
