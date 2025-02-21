package com.dsa.qtrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.ui.login.LoginActivity
import com.dsa.qtrack.ui.solicitud.SolicitudViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var solicitudViewModel: SolicitudViewModel
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

        solicitudViewModel = SolicitudViewModel()

        solicitudViewModel.solicitudes.observe(this) { solicitudes ->
            Log.d("MainActivity", "Solicitudes actualizadas: ${solicitudes.size} elementos recibidos.")
            solicitudAdapter.updateSolicitudes(solicitudes)
        }

        Log.d("MainActivity", "Solicitando actualización de solicitudes.")
        solicitudViewModel.fetchSolicitudes()
    }

    private fun checkSession(): Boolean {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN", null)
        Log.d("MainActivity", "Token almacenado: $token")
        return !token.isNullOrEmpty()
    }

    // 🔥 Borra la sesión al cerrar la app
    override fun onStop() {
        super.onStop()

        // Verifica si la app se va a segundo plano y se cierra la tarea
        if (isFinishing) {
            clearSession()
        }
    }

    private fun clearSession() {
        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        Log.d("MainActivity", "Sesión cerrada y token eliminado.")
    }

}
