package com.dsa.qtrack

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dsa.qtrack.data.model.Solicitud

class SolicitudAdapter(private var solicitudes: List<Solicitud>) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    inner class SolicitudViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val solicitudNumTextView: TextView = view.findViewById(R.id.solicitudNum)
        val clienteTextView: TextView = view.findViewById(R.id.cliente)
        val estatusTextView: TextView = view.findViewById(R.id.estatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud, parent, false)
        return SolicitudViewHolder(view)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        val solicitud = solicitudes[position]
        holder.solicitudNumTextView.text = "#${solicitud.solicitud_num}"
        holder.clienteTextView.text = solicitud.cliente ?: "N/A"
        holder.estatusTextView.text = solicitud.estatus ?: "Pendiente"
    }

    override fun getItemCount() = solicitudes.size

    fun updateSolicitudes(nuevasSolicitudes: List<Solicitud>) {
        solicitudes = nuevasSolicitudes
        notifyDataSetChanged()
    }
}