package com.dsa.qtrack.data.model

data class Solicitud(
    val id: Int,
    val id_mensajero: Int,
    val solicitud_num: String = "",
    val nombre_completo: String? = null,
    val cliente: String? = null,
    val direccion: String? = null,
    val prioridad: String? = null,
    val estatus: String? = null,
    val seguimiento: String? = null,
    val fecha_entrega: String? = null
)