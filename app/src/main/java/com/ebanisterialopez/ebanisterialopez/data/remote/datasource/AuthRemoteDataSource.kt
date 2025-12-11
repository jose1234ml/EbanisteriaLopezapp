package com.ebanisterialopez.ebanisterialopez.data.remote.datasource


import com.ebanisterialopez.ebanisterialopez.data.remote.AuthApi
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.AuthRequestDto
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import javax.inject.Inject

interface AuthRemoteDataSource {
    suspend fun login(email: String, password: String): AuthToken
    suspend fun register(email: String, password: String): AuthToken
}

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

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