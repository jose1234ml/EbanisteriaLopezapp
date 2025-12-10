package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject
class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<AuthToken> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return Resource.Error("Email y contraseña no pueden estar vacíos.")
            }
            val result = repository.login(email, password)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error("Error de login: ${e.localizedMessage ?: "Error desconocido"}")
        }
    }
}