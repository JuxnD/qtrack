package com.dsa.qtrack.data.model

data class QtrackResponse(
    val data: DataWrapper?
)

data class DataWrapper(
    val rows: List<Solicitud>
)
