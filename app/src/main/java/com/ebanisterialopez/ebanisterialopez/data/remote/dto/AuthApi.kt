package com.ebanisterialopez.ebanisterialopez.data.remote.dto

import retrofit2.http.Body
import retrofit2.http.POST
interface AuthApi {

    @POST("api/Usuarios/login")
    suspend fun login(
        @Body request: AuthRequestDto
    ): AuthResponseDto
    @POST("api/Usuarios/register")
    suspend fun register(
        @Body request: AuthRequestDto
    ): AuthResponseDto
}