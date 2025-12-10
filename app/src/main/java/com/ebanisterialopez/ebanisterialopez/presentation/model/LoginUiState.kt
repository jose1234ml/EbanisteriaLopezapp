package com.ebanisterialopez.ebanisterialopez.presentation.model
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false,
    val authToken: String? = null,
    val email: String? = null
)