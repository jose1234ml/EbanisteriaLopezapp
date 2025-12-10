package com.ebanisterialopez.ebanisterialopez.presentation.ajuste

import androidx.lifecycle.ViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.model.SettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()
    fun toggleDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isDarkModeEnabled = enabled)
    }
    fun toggleNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(receiveNotifications = enabled)
    }
    fun setLanguage(language: String) {
        _uiState.value = _uiState.value.copy(appLanguage = language)
    }
    fun setCurrency(currency: String) {
        _uiState.value = _uiState.value.copy(showPriceCurrency = currency)
    }
}