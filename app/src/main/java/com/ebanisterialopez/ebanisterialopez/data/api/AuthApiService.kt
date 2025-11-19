package com.ebanisterialopez.ebanisterialopez.data.api

import com.ebanisterialopez.ebanisterialopez.data.api.model.AuthResponse
import com.ebanisterialopez.ebanisterialopez.data.api.model.CreateUserRequest
import com.ebanisterialopez.ebanisterialopez.data.api.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {


    @POST("api/Auth/Login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>


    @POST("api/Usuarios")
    suspend fun createUser(@Body request: CreateUserRequest): Response<AuthResponse>
}