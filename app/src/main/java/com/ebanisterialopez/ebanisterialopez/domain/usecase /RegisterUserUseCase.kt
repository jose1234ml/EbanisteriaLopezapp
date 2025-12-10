package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject
class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<AuthToken> {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return Resource.Error("Todos los campos son obligatorios.")
            }

            val result = repository.register(email, password)
            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error("Error de registro: ${e.localizedMessage ?: "Error desconocido"}")
        }
    }
}