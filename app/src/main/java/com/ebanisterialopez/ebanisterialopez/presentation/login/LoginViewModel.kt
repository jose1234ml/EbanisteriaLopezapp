package com.ebanisterialopez.ebanisterialopez.presentation.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.usecase.CreateUserUseCase
import com.ebanisterialopez.ebanisterialopez.domain.usecase.GetAuthStateUseCase
import com.ebanisterialopez.ebanisterialopez.domain.usecase.LoginUseCase
import com.ebanisterialopez.ebanisterialopez.presentation.login.model.CreateUserState
import com.ebanisterialopez.ebanisterialopez.presentation.login.model.LoginEvent
import com.ebanisterialopez.ebanisterialopez.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getAuthStateUseCase().collect { isAuth ->
                _state.update { it.copy(isAuthenticated = isAuth) }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnUserChange -> _state.update { it.copy(loginUserInput = event.user) }
            is LoginEvent.OnPasswordChange -> _state.update { it.copy(loginPasswordInput = event.password) }
            is LoginEvent.PerformLogin -> performLogin()
            is LoginEvent.UserMessageShown -> _state.update { it.copy(userMessage = null) }
            is LoginEvent.ShowCreateUserDialog -> _state.update { it.copy(showCreateUserDialog = true) }
            is LoginEvent.HideCreateUserDialog -> _state.update { it.copy(showCreateUserDialog = false, createUserState = CreateUserState()) }

            is LoginEvent.OnCreateNameChange -> _state.update { it.copy(createUserState = it.createUserState.copy(nameInput = event.name)) }
            is LoginEvent.OnCreatePasswordChange -> _state.update { it.copy(createUserState = it.createUserState.copy(passwordInput = event.password)) }
            is LoginEvent.PerformCreateUser -> performCreateUser()
        }
    }

    private fun performLogin() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, userMessage = null) }

        val userName = _state.value.loginUserInput
        val password = _state.value.loginPasswordInput

        if (userName.isBlank() || password.isBlank()) {
            _state.update { it.copy(isLoading = false, userMessage = "Usuario y contraseña son requeridos.") }
            return@launch
        }

        when (val result = loginUseCase(userName, password)) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, userMessage = "¡Inicio de sesión exitoso! Bienvenido.") }
                _navigationEvent.emit(Unit)
            }
            is Resource.Error -> {
                _state.update { it.copy(isLoading = false, userMessage = result.message) }
            }
            is Resource.Loading -> _state.update { it.copy(isLoading = true) }
            else -> _state.update { it.copy(isLoading = false) }
        }
    }

    private fun performCreateUser() = viewModelScope.launch {
        _state.update { it.copy(createUserState = it.createUserState.copy(isLoading = true)) }

        val state = _state.value.createUserState

        if (state.nameInput.isBlank() || state.passwordInput.isBlank()) {
            _state.update { it.copy(createUserState = it.createUserState.copy(isLoading = false), userMessage = "El nombre de usuario y la contraseña son obligatorios.") }
            return@launch
        }

        when (val result = createUserUseCase(state.nameInput, state.passwordInput, state.nameInput)) {
            is Resource.Success -> {
                _state.update { it.copy(
                    createUserState = it.createUserState.copy(isLoading = false),
                    showCreateUserDialog = false,
                    userMessage = "Usuario creado y sesión iniciada con éxito."
                ) }
                _navigationEvent.emit(Unit)
            }
            is Resource.Error -> {
                _state.update { it.copy(createUserState = it.createUserState.copy(isLoading = false), userMessage = result.message) }
            }
            is Resource.Loading -> _state.update { it.copy(createUserState = it.createUserState.copy(isLoading = true)) }
            else -> _state.update { it.copy(createUserState = it.createUserState.copy(isLoading = false)) }
        }
    }
}