package com.ebanisterialopez.ebanisterialopez.presentation.login

import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.usecase.LoginUseCase
import com.ebanisterialopez.ebanisterialopez.presentation.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state
    internal val stateForTest = _state
    fun onLoginClick(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            when (val result = loginUseCase(email, password)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        authToken = result.data?.token,
                        email = email
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> { /* Ignorado */ }
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            _state.value = LoginUiState()
        }
    }
}