package com.ebanisterialopez.ebanisterialopez.data.repository



import com.ebanisterialopez.ebanisterialopez.data.api.AuthApiService
import com.ebanisterialopez.ebanisterialopez.data.api.model.AuthResponse
import com.ebanisterialopez.ebanisterialopez.data.api.model.CreateUserRequest
import com.ebanisterialopez.ebanisterialopez.data.api.model.LoginRequest
import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val api: AuthApiService
) {
    suspend fun login(request: LoginRequest): Resource<AuthResponse> {
        return try {
            val response = api.login(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("Credenciales inválidas o error HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red/servidor")
        }
    }

    suspend fun createUser(request: CreateUserRequest): Resource<AuthResponse> {
        return try {
            val response = api.createUser(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("Fallo al crear usuario: HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de red al registrar usuario")
        }
    }
}