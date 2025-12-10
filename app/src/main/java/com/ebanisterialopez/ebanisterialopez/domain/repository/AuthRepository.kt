package com.ebanisterialopez.ebanisterialopez.domain.repository

import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
interface AuthRepository {
    suspend fun login(email: String, password: String): AuthToken
    suspend fun register(email: String, password: String): AuthToken
}