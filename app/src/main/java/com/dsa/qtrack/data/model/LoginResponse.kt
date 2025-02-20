package com.dsa.qtrack.data.model

data class LoginResponse(
    val message: String,
    val user: User,
    val token: String
)