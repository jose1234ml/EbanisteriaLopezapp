package com.ebanisterialopez.ebanisterialopez.data.remote.dto

import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.google.gson.annotations.SerializedName
data class AuthResponseDto(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("userId")
    val userId: Int? = null
) {
    fun toAuthToken(): AuthToken {
        return AuthToken(
            token = token ?: "",
            userId = userId ?: -1
        )
    }
}