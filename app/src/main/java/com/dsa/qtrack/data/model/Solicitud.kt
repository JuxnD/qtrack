package com.dsa.qtrack.data.model

data class Solicitud(
    val id: Int,
    val numero: Int?,
    val id_mensajero: Int?,
    val solicitud_num: String = "",
    val id_usuario: Int,
    val id_destinatario: Int?,
    val id_cliente: Int,
    val nombre_completo: String?,
    val email: String?,
    val telefono: String?,
    val cliente: String?,
    val item_1: String?,
    val item_2: String?,
    val item_3: String?,
    val item_4: String?,
    val item_5: String?,
    val item_6: String?,
    val item_7: String?,
    val item_8: String?,
    val item_9: String?,
    val item_10: String?,
    val descripcion: String?,
    val tipo: String?,
    val para: String?,
    val entregado_por: String?,
    val destinatario_ext: String?,
    val descripcion_docs: String?,
    val prioridad: String?,
    val prioridad_asig: String?,
    val confir_entrega: String?,
    val direccion: String?,
    val direccion_recogida: String?,
    val zona: String?,
    val comentarios: String?,
    val cedula_pr: String?,
    val cargo_pr: String?,
    val nombre_pr: String?,
    val nombre_pe: String?,
    val cedula_pe: String?,
    val seguimiento: String?,
    val estatus: String?,
    val creado: String?,
    val fecha_inicio: String?,
    val fecha_asignado: String?,
    val fecha_enrecogida: String?,
    val fecha_recogida: String?,
    val fecha_recogido: String?,
    val fecha_entrega: String?,
    val fecha_entregado: String?,
    val fecha_encamino: String?,
    val fecha_devuelto: String?
)
