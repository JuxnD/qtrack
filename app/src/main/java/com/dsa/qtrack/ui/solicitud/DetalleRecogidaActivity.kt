package com.dsa.qtrack.ui.solicitud

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dsa.qtrack.R
import android.widget.TextView

class DetalleRecogidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_recogida)

        val tipoSolicitud = intent.getStringExtra("tipo")
        val solicitudNumero = intent.getStringExtra("solicitud_num")
        val prioridad = intent.getStringExtra("prioridad")
        val fechaInicio = intent.getStringExtra("fecha_inicio")
        val cliente = intent.getStringExtra("cliente")
        val destinatario = intent.getStringExtra("destinatario")
        val direccion = intent.getStringExtra("direccion")
        val descripcion = intent.getStringExtra("descripcion_docs")

        Log.d("DetalleRecogidaActivity", "Datos recibidos -> Tipo: $tipoSolicitud, Número: $solicitudNumero, Prioridad: $prioridad")

        findViewById<TextView>(R.id.tvTipoSolicitudDetalle).text = "$tipoSolicitud - #$solicitudNumero"
        findViewById<TextView>(R.id.tvPrioridadDetalle).text = "Prioridad: $prioridad"
        findViewById<TextView>(R.id.tvFechaInicioDetalle).text = "Fecha de inicio: $fechaInicio"
        findViewById<TextView>(R.id.tvClienteDetalle).text = "Cliente: $cliente"
        findViewById<TextView>(R.id.tvDestinatarioDetalle).text = "Destinatario: $destinatario"
        findViewById<TextView>(R.id.tvDireccionDetalle).text = "Dirección: $direccion"
        findViewById<TextView>(R.id.tvDescripcionDetalle).text = "Descripción: $descripcion"

        findViewById<TextView>(R.id.btnVolver).setOnClickListener {
            finish()
        }
    }
}
