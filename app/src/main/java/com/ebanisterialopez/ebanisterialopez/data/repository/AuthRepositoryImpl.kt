package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.AuthApi
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.AuthRequestDto
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthToken {
        val cleanedEmail = email.trim()
        val cleanedPassword = password.trim()
        val request = AuthRequestDto(cleanedEmail, cleanedPassword)
        return api.login(request).toAuthToken()
    }

    override suspend fun register(email: String, password: String): AuthToken {
        val cleanedEmail = email.trim()
        val cleanedPassword = password.trim()
        val request = AuthRequestDto(cleanedEmail, cleanedPassword)
        return api.register(request).toAuthToken()
    }
}