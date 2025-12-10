package com.ebanisterialopez.ebanisterialopez.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class SessionManager @Inject constructor() {
    private val _authTokenFlow = MutableStateFlow<String?>(null)
    val authTokenFlow: Flow<String?> = _authTokenFlow.asStateFlow()
    suspend fun saveAuthToken(token: String) {
        _authTokenFlow.value = token
    }
    suspend fun clearSession() {
        _authTokenFlow.value = null
    }
}