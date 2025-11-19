package com.ebanisterialopez.ebanisterialopez.domain.usecase


import com.ebanisterialopez.ebanisterialopez.data.api.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthModel
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(userName: String, password: String, name: String): Resource<AuthModel> =
        repo.createUser(userName, password, name)
}