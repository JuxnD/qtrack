package com.dsa.qtrack.data.model

import com.dsa.qtrack.Solicitud

data class QtrackResponse(
    val data: DataWrapper?
)

data class DataWrapper(
    val rows: List<Solicitud>
)