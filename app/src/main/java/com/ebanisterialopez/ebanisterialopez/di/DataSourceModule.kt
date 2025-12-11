package com.ebanisterialopez.ebanisterialopez.di

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.AuthRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.AuthRemoteDataSourceImpl
import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.ProductRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.ProductRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProductRemoteDataSource(
        impl: ProductRemoteDataSourceImpl
    ): ProductRemoteDataSource
}