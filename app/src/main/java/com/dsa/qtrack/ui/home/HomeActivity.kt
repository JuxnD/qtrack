package com.dsa.qtrack.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.dsa.qtrack.R
import com.dsa.qtrack.api.ApiClient
import com.dsa.qtrack.data.Api.QtrackApiService
import com.dsa.qtrack.session.SessionManager
import com.dsa.qtrack.ui.login.LoginActivity
import com.dsa.qtrack.ui.solicitud.SolicitudesByMensajeroActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var tvOpenRequests: TextView
    private lateinit var tvClosedRequests: TextView
    private lateinit var tvAssignedRequests: TextView
    private lateinit var tvCompletedRequests: TextView
    private lateinit var apiService: QtrackApiService
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sessionManager = SessionManager(this)
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión no válida. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            return
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        initViews()
        tvUserName.text = "👤 ${sessionManager.getUserName()}"

        apiService = ApiClient.retrofit.create(QtrackApiService::class.java)
        fetchUserRequests(sessionManager.getUserId())

        setupMenuNavigation()
    }

    private fun initViews() {
        tvUserName = findViewById(R.id.tvUserName)
        tvOpenRequests = findViewById(R.id.cardOpenRequests).findViewById(R.id.tvStatValue)
        tvClosedRequests = findViewById(R.id.cardClosedRequests).findViewById(R.id.tvStatValue)
        tvAssignedRequests = findViewById(R.id.cardAssignedRequests).findViewById(R.id.tvStatValue)
        tvCompletedRequests = findViewById(R.id.cardCompletedRequests).findViewById(R.id.tvStatValue)
    }

    private fun fetchUserRequests(userId: Int) {
        lifecycleScope.launch {
            try {
                val response = apiService.getSolicitudesByMensajero(userId)
                if (response.isSuccessful) {
                    val solicitudes = response.body()?.data?.rows ?: emptyList()

                    tvOpenRequests.text = solicitudes.count { it.estatus.equals("abierto", ignoreCase = true) }.toString()
                    tvClosedRequests.text = solicitudes.count { it.estatus.equals("cerrado", ignoreCase = true) }.toString()
                    tvAssignedRequests.text = solicitudes.count { it.seguimiento.equals("Asignada", ignoreCase = true) }.toString()
                    tvCompletedRequests.text = solicitudes.count { it.seguimiento.equals("Entregado", ignoreCase = true) }.toString()
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener datos: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMenuNavigation() {
        findViewById<CardView>(R.id.cardMisSolicitudes).setOnClickListener {
            startActivity(Intent(this, SolicitudesByMensajeroActivity::class.java))
        }

        findViewById<CardView>(R.id.cardTodasSolicitudes).setOnClickListener {
            Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
        }

        findViewById<CardView>(R.id.cardSolicitudesCerradas).setOnClickListener {
            Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show()
        }

        findViewById<CardView>(R.id.cardConfiguracion).setOnClickListener {
            Toast.makeText(this, "Configuración no disponible aún", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
