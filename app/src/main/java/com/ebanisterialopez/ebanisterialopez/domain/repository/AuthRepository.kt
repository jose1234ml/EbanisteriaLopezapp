package com.ebanisterialopez.ebanisterialopez.domain.repository


import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(userName: String, password: String): Resource<AuthModel>
    suspend fun createUser(userName: String, password: String, name: String): Resource<AuthModel>
    fun isAuthenticated(): Flow<Boolean>
    suspend fun logout()
}