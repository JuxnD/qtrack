package com.dsa.qtrack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.View.SolicitudViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var solicitudViewModel: SolicitudViewModel
    private lateinit var solicitudAdapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        solicitudAdapter = SolicitudAdapter(emptyList())
        recyclerView.adapter = solicitudAdapter

        solicitudViewModel = ViewModelProvider(this).get(SolicitudViewModel::class.java)
        solicitudViewModel.solicitudes.observe(this) { solicitudes ->
            solicitudAdapter.updateSolicitudes(solicitudes)
        }

        solicitudViewModel.fetchSolicitudes()
    }
}