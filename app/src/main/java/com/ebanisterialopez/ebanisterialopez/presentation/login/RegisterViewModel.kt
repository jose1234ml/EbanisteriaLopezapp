package com.ebanisterialopez.ebanisterialopez.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.usecase.RegisterUseCase
import com.ebanisterialopez.ebanisterialopez.presentation.model.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state
    fun onRegisterClick(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = registerUseCase(email, password)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        authToken = result.data?.token
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
}