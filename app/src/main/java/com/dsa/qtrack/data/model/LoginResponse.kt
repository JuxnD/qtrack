package com.dsa.qtrack.model

import com.dsa.qtrack.data.model.User

data class LoginResponse(
    val message: String,
    val user: User,
    val token: String
)
