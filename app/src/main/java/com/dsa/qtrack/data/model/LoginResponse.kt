package com.dsa.qtrack.model

data class User(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String,
    val status: String
)

data class LoginResponse(
    val message: String,
    val user: User,
    val token: String
)
