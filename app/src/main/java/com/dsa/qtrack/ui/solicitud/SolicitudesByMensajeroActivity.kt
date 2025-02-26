package com.dsa.qtrack.ui.solicitud

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R
import com.dsa.qtrack.viewmodel.SolicitudViewModel

class SolicitudesByMensajeroActivity : AppCompatActivity() {

    private val viewModel: SolicitudViewModel by viewModels()
    private lateinit var adapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitudes_by_mensajero)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewSolicitudes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SolicitudAdapter(listOf())
        recyclerView.adapter = adapter

        viewModel.getSolicitudesAbiertas().observe(this) { solicitudes ->
            adapter.actualizarLista(solicitudes)
        }

        viewModel.obtenerSolicitudesAbiertas()
    }
}
