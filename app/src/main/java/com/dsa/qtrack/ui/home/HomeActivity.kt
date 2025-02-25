package com.dsa.qtrack.ui.home

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme.colors
import com.dsa.qtrack.R
import com.dsa.qtrack.data.model.Solicitud
import com.dsa.qtrack.api.ApiClient
import com.dsa.qtrack.data.Api.QtrackApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class HomeActivity : AppCompatActivity() {

    private lateinit var apiService: QtrackApiService
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        barChart = findViewById(R.id.barChart)
        apiService = ApiClient.retrofit.create(QtrackApiService::class.java)

        val sharedPreferences = getSharedPreferences("QTrackPrefs", MODE_PRIVATE)
        val idMensajero = sharedPreferences.getInt("id_mensajero", -1)
        if (idMensajero != -1) {
            obtenerSolicitudes(idMensajero)
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerSolicitudes(idMensajero: Int) {
        apiService.getSolicitudesByMensajero(idMensajero).enqueue(object :
            Callback<List<Solicitud>> {
            override fun onResponse(call: Call<List<Solicitud>>, response: Response<List<Solicitud>>) {
                if (response.isSuccessful) {
                    response.body()?.let { solicitudes ->
                        actualizarUI(solicitudes)
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Error al obtener solicitudes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Solicitud>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Fallo: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun actualizarUI(solicitudes: List<Solicitud>) {
        val abiertas = solicitudes.count { it.estado == "abierto" }
        val cerradas = solicitudes.count { it.estado == "cerrado" }
        val asignadas = solicitudes.size
        val cumplimiento = if (asignadas != 0) (cerradas * 100) / asignadas else 0

        findViewById<TextView>(R.id.tvOpenCount).text = "$abiertas"
        findViewById<TextView>(R.id.tvClosedCount).text = "$cerradas"
        findViewById<TextView>(R.id.tvAssignedCount).text = "$asignadas"
        findViewById<TextView>(R.id.tvCompliance).text = "$cumplimiento%"

        mostrarGrafico(asignadas, cerradas)
    }

    private fun mostrarGrafico(asignadas: Int, cerradas: Int) {
        val entries = listOf(
            BarEntry(0f, asignadas.toFloat()),
            BarEntry(1f, cerradas.toFloat())
        )

        val dataSet = BarDataSet(entries, "Solicitudes").apply {
            colors = listOf(Color.BLUE, Color.GREEN)
            valueTextSize = 14f
        }

        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(listOf("Asignadas", "Cerradas"))
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }
            animateY(1000)
            invalidate()
        }
    }
}
