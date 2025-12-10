package com.ebanisterialopez.ebanisterialopez.presentation.model

data class SettingsState(
    val isDarkModeEnabled: Boolean = false,
    val receiveNotifications: Boolean = true,
    val appLanguage: String = "Español",
    val showPriceCurrency: String = "DOP"
)