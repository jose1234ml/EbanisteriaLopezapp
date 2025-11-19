package com.ebanisterialopez.ebanisterialopez.data.repository.mapper



import com.ebanisterialopez.ebanisterialopez.data.api.model.AuthResponse
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthModel
import javax.inject.Inject


    class AuthMapper @Inject constructor() {
        fun toDomain(response: AuthResponse): AuthModel {
            return AuthModel(
                userId = response.usuarioId ?: 0,
                userName = response.userName ?: "",
                token = response.token ?: ""
            )
        }
    }
