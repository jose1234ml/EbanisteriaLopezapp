package com.ebanisterialopez.ebanisterialopez.di


import com.ebanisterialopez.ebanisterialopez.data.repository.AuthRepositoryImpl
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}