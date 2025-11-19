package com.ebanisterialopez.ebanisterialopez.data.api.model

data class AuthResponse(
    val usuarioId: Int,
    val userName: String,
    val token: String
)