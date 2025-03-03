package com.dsa.qtrack.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R
import com.dsa.qtrack.data.api.ApiClient
import com.dsa.qtrack.data.api.QtrackApiService
import com.dsa.qtrack.ui.login.LoginActivity
import com.dsa.qtrack.ui.login.SessionManager
import com.dsa.qtrack.ui.solicitud.SolicitudesByMensajeroActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var tvUserName: TextView
    private lateinit var recyclerViewStats: RecyclerView
    private lateinit var recyclerViewMenu: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: QtrackApiService

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

        tvUserName = findViewById(R.id.tvUserName)
        recyclerViewStats = findViewById(R.id.viewPagerStats)
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu)

        tvUserName.text = "👤 ${sessionManager.getUserName()}"

        setupStatsRecyclerView()
        setupMenuRecyclerView()

        apiService = ApiClient.retrofit.create(QtrackApiService::class.java)
        fetchUserRequests(sessionManager.getUserId())
    }

    private fun setupStatsRecyclerView() {
        recyclerViewStats.layoutManager = GridLayoutManager(this, 2)
        val stats = listOf(
            HomeStat("Abiertas", "0"),
            HomeStat("Cerradas", "0"),
            HomeStat("Asignadas", "0"),
            HomeStat("Cumplidas", "0")
        )
        recyclerViewStats.adapter = HomeStatAdapter(stats)
    }

    private fun setupMenuRecyclerView() {
        recyclerViewMenu.layoutManager = GridLayoutManager(this, 2)
        val menuItems = listOf(
            HomeMenuItem("Mis Solicitudes", R.drawable.ic_list) {
                val intent = Intent(this, SolicitudesByMensajeroActivity::class.java)
                intent.putExtra("id_mensajero", sessionManager.getUserId())
                startActivity(intent)
            },
            HomeMenuItem("Cerrar Sesión", R.drawable.ic_logout) {
                sessionManager.logout()
                Toast.makeText(this, "Sesión cerrada.", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        )
        recyclerViewMenu.adapter = HomeMenuAdapter(menuItems)
    }

    private fun fetchUserRequests(userId: Int) {
        lifecycleScope.launch {
            try {
                val response = apiService.getSolicitudesByMensajero(userId)
                if (response.isSuccessful) {
                    val solicitudes = response.body()?.data?.rows ?: emptyList()

                    val updatedStats = listOf(
                        HomeStat("Abiertas", solicitudes.count { it.estatus.equals("abierto", ignoreCase = true) }.toString()),
                        HomeStat("Cerradas", solicitudes.count { it.estatus.equals("cerrado", ignoreCase = true) }.toString()),
                        HomeStat("Asignadas", solicitudes.count { it.seguimiento.equals("Asignada", ignoreCase = true) }.toString()),
                        HomeStat("Cumplidas", solicitudes.count { it.seguimiento.equals("Entregado", ignoreCase = true) }.toString())
                    )

                    (recyclerViewStats.adapter as? HomeStatAdapter)?.updateStats(updatedStats)
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener datos: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        sessionManager.logout()
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }


    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
