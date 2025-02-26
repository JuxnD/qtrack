package com.dsa.qtrack.ui.solicitud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.R
import com.dsa.qtrack.data.model.Solicitud

class SolicitudAdapter(private var solicitudes: List<Solicitud>) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    inner class SolicitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumeroSolicitud: TextView = itemView.findViewById(R.id.tvSolicitudNumero)
        val tvTipoSolicitud: TextView = itemView.findViewById(R.id.tvTipoSolicitud)
        val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
        val tvPrioridad: TextView = itemView.findViewById(R.id.tvPrioridad)
        val tvFechaInicio: TextView = itemView.findViewById(R.id.tvFechaInicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud, parent, false)
        return SolicitudViewHolder(view)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        val solicitud = solicitudes[position]
        holder.tvNumeroSolicitud.text = "#${solicitud.solicitud_num}"
        holder.tvTipoSolicitud.text = solicitud.tipo ?: "Tipo no disponible"
        holder.tvCliente.text = solicitud.cliente ?: "Cliente no disponible"
        holder.tvPrioridad.text = "Prioridad: ${solicitud.prioridad ?: "N/A"}"
        holder.tvFechaInicio.text = "Inicio: ${solicitud.fecha_inicio ?: "Sin fecha"}"
    }

    override fun getItemCount(): Int = solicitudes.size

    fun actualizarLista(nuevasSolicitudes: List<Solicitud>) {
        solicitudes = nuevasSolicitudes
        notifyDataSetChanged()
    }
}