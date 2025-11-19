package com.ebanisterialopez.ebanisterialopez.data.api.model


data class LoginRequest(
    val userName: String,
    val password: String
)

data class CreateUserRequest(
    val userName: String,
    val password: String
)