// RepositoryModule.kt
package com.ebanisterialopez.ebanisterialopez.di

import com.ebanisterialopez.ebanisterialopez.data.repository.CarritoRepositoryImpl
import com.ebanisterialopez.ebanisterialopez.data.repository.ProductRepositoryImpl
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarritoRepository(
        impl: CarritoRepositoryImpl
    ): CarritoRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository
}