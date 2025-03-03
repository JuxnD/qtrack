package com.dsa.qtrack.ui.solicitud

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R
import com.dsa.qtrack.data.model.Solicitud

class SolicitudAdapter(private var solicitudes: List<Solicitud>) : RecyclerView.Adapter<SolicitudAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tipoSolicitud: TextView = view.findViewById(R.id.tvTipoSolicitud)
        val solicitudNum: TextView = view.findViewById(R.id.tvSolicitudNumero)
        val prioridad: TextView = view.findViewById(R.id.tvPrioridad)
        val cliente: TextView   = view.findViewById(R.id.tvCliente)
        val para   : TextView   = view.findViewById(R.id.tvDestinatario)
        val fechaInicio : TextView = view.findViewById(R.id.tvFechaInicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val solicitud = solicitudes[position]

        holder.solicitudNum.text = "#${solicitud.solicitud_num}"
        holder.tipoSolicitud.text = solicitud.tipo ?: "Sin tipo"
        holder.prioridad.text = solicitud.prioridad ?: "No asignada"
        holder.fechaInicio.text = solicitud.fecha_inicio ?: "Sin fecha"
        holder.cliente.text = solicitud.cliente ?: "Sin cliente"
        holder.para.text = "Para: ${solicitud.nombre_completo ?: "No asignado"}"

        // 🚀 Agrega Logs para verificar datos en Logcat
        Log.d("SolicitudAdapter", "Mostrando solicitud ${solicitud.solicitud_num} - Cliente: ${solicitud.cliente} - Para: ${solicitud.nombre_completo}")

        // ✅ Agregar evento de clic al item
        holder.itemView.setOnClickListener {
            Log.d("SolicitudAdapter", "Click en solicitud: ${solicitud.solicitud_num} - Tipo: ${solicitud.tipo}")

            val intent = if (solicitud.tipo == "Recogida de documentos") {
                Intent(holder.itemView.context, DetalleRecogidaActivity::class.java)
            } else {
                Intent(holder.itemView.context, DetalleRemisionActivity::class.java)
            }

            // Pasar datos a la actividad de detalle
            intent.putExtra("solicitud_num", solicitud.solicitud_num)
            intent.putExtra("tipo", solicitud.tipo)
            intent.putExtra("prioridad", solicitud.prioridad)
            intent.putExtra("fecha_inicio", solicitud.fecha_inicio)
            intent.putExtra("cliente", solicitud.cliente)
            intent.putExtra("destinatario", solicitud.nombre_completo)
            intent.putExtra("direccion", solicitud.direccion)
            intent.putExtra("descripcion_docs", solicitud.descripcion_docs)

            Log.d("SolicitudAdapter", "Intent creado para abrir: ${intent.component?.className}")

            holder.itemView.context.startActivity(intent)
        }
    }

    fun actualizarLista(nuevaLista: List<Solicitud>) {
        solicitudes = nuevaLista
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = solicitudes.size

    private fun abrirDetalle(context: Context, solicitud: Solicitud) {
        Log.d("SolicitudAdapter", "Método abrirDetalle ejecutado con tipo: ${solicitud.tipo}")

        if (solicitud.tipo.isNullOrBlank()) {
            Toast.makeText(context, "Error: Tipo de solicitud no especificado", Toast.LENGTH_LONG).show()
            Log.e("SolicitudAdapter", "Error: Tipo de solicitud no especificado para solicitud ${solicitud.solicitud_num}")
            return
        }

        val intent = if (solicitud.tipo.trim() == "Recogida de documentos") {
            Intent(context, DetalleRecogidaActivity::class.java)
        } else {
            Intent(context, DetalleRemisionActivity::class.java)
        }

        intent.putExtra("solicitud_num", solicitud.solicitud_num ?: "Desconocido")
        intent.putExtra("tipo", solicitud.tipo ?: "Desconocido")
        intent.putExtra("prioridad", solicitud.prioridad ?: "No asignada")
        intent.putExtra("fecha_inicio", solicitud.fecha_inicio ?: "Sin fecha")
        intent.putExtra("cliente", solicitud.cliente ?: "Sin cliente")
        intent.putExtra("destinatario", solicitud.nombre_completo ?: "No especificado")
        intent.putExtra("direccion", solicitud.direccion ?: "No disponible")
        intent.putExtra("descripcion_docs", solicitud.descripcion_docs ?: "No hay descripción")

        Log.d("SolicitudAdapter", "Intent creado con extras: $intent")

        try {
            context.startActivity(intent)
            Log.d("SolicitudAdapter", "Actividad iniciada correctamente")
        } catch (e: Exception) {
            Log.e("SolicitudAdapter", "Error al iniciar actividad de detalle: ${e.message}", e)
        }
    }

}
