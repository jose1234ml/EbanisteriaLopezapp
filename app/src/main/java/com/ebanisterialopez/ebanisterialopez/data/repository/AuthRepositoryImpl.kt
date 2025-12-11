package com.ebanisterialopez.ebanisterialopez.data.repository


import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.AuthRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthToken {
        return remoteDataSource.login(email, password)
    }

    override suspend fun register(email: String, password: String): AuthToken {
        return remoteDataSource.register(email, password)
    }
}