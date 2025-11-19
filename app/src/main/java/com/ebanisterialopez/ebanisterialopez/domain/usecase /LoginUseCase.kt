package com.ebanisterialopez.ebanisterialopez.domain.usecase


import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthModel
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(userName: String, password: String): Resource<AuthModel> =
        repo.login(userName, password)
}