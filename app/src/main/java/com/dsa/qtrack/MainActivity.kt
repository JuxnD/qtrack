package com.dsa.qtrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.ui.login.LoginActivity
import com.dsa.qtrack.ui.solicitud.SolicitudAdapter
import com.dsa.qtrack.ui.solicitud.SolicitudViewModel

class MainActivity : AppCompatActivity() {

    private val solicitudViewModel: SolicitudViewModel by viewModels()
    private lateinit var solicitudAdapter: SolicitudAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔐 Verifica la sesión al iniciar
        if (!checkSession()) {
            Log.d("MainActivity", "No hay sesión activa. Redirigiendo a LoginActivity.")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        solicitudAdapter = SolicitudAdapter(emptyList())
        recyclerView.adapter = solicitudAdapter

        // ✅ Actualización aquí: Observa directamente la propiedad solicitudesAbiertas
        solicitudViewModel.solicitudesAbiertas.observe(this) { solicitudes ->
            Log.d("MainActivity", "Solicitudes actualizadas: ${solicitudes.size} elementos recibidos.")
            solicitudAdapter.actualizarLista(solicitudes)
        }

        // ✅ Cambia a fetchSolicitudesAbiertas para iniciar la carga
        Log.d("MainActivity", "Solicitando actualización de solicitudes.")
        solicitudViewModel.fetchSolicitudesAbiertas(this)
    }

    private fun checkSession(): Boolean {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        Log.d("MainActivity", "Token almacenado: $token")
        return !token.isNullOrEmpty()
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing) {
            clearSession()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearSession()
    }

    private fun clearSession() {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        Log.d("MainActivity", "Sesión cerrada y token eliminado.")
    }
}
