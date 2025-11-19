package com.ebanisterialopez.ebanisterialopez.presentation.login.model


data class CreateUserState(
    val nameInput: String = "",
    val passwordInput: String = "",
    val isLoading: Boolean = false
)

data class LoginUiState(
    val loginUserInput: String = "",
    val loginPasswordInput: String = "",
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val showCreateUserDialog: Boolean = false,
    val createUserState: CreateUserState = CreateUserState()
)