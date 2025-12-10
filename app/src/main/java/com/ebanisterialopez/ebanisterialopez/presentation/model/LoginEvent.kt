package com.ebanisterialopez.ebanisterialopez.presentation.model
sealed interface LoginEvent {
    data class OnUserChange(val user: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    object PerformLogin : LoginEvent
    object UserMessageShown : LoginEvent
    object ShowCreateUserDialog : LoginEvent
    object HideCreateUserDialog : LoginEvent

    data class OnCreateNameChange(val name: String) : LoginEvent
    data class OnCreatePasswordChange(val password: String) : LoginEvent
    object PerformCreateUser : LoginEvent
}