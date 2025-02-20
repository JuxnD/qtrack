package com.dsa.qtrack.model

import com.dsa.qtrack.Solicitud

data class QtrackResponse(
    val data: DataWrapper?
)

data class DataWrapper(
    val rows: List<Solicitud>
)