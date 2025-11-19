package com.ebanisterialopez.ebanisterialopez.data.repository


import com.ebanisterialopez.ebanisterialopez.data.api.model.CreateUserRequest
import com.ebanisterialopez.ebanisterialopez.data.api.model.LoginRequest
import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import com.ebanisterialopez.ebanisterialopez.data.repository.mapper.AuthMapper
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthModel
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import com.ebanisterialopez.ebanisterialopez.util.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val sessionManager: SessionManager,
    private val mapper: AuthMapper
) : AuthRepository {

    override suspend fun login(userName: String, password: String): Resource<AuthModel> {
        val request = LoginRequest(userName, password)
        return when (val result = remoteDataSource.login(request)) {
            is Resource.Success -> {
                result.data?.let { response ->
                    sessionManager.saveAuthToken(response.token)
                    Resource.Success(mapper.toDomain(response))
                } ?: Resource.Error("Error de respuesta")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Fallo al iniciar sesión")
            else -> Resource.Loading()
        }
    }

    override suspend fun createUser(userName: String, password: String, name: String): Resource<AuthModel> {

        val request = CreateUserRequest(userName, password)
        return when (val result = remoteDataSource.createUser(request)) {
            is Resource.Success -> {
                result.data?.let { response ->
                    sessionManager.saveAuthToken(response.token)
                    Resource.Success(mapper.toDomain(response))
                } ?: Resource.Error("Error de respuesta")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Fallo al crear usuario")
            else -> Resource.Loading()
        }
    }

    override fun isAuthenticated(): Flow<Boolean> {
        return sessionManager.authTokenFlow.map { !it.isNullOrBlank() }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }
}