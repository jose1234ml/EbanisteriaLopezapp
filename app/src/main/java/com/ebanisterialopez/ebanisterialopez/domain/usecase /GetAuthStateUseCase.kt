package com.ebanisterialopez.ebanisterialopez.domain.usecase


import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> = repo.isAuthenticated()
}