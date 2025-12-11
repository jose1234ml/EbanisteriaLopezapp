package com.ebanisterialopez.ebanisterialopez.di

import com.ebanisterialopez.ebanisterialopez.data.datasource.local.CarritoLocalDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.VentaRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.AuthApi
import com.ebanisterialopez.ebanisterialopez.data.remote.ProductApi
import com.ebanisterialopez.ebanisterialopez.data.remote.SalesApi
import com.ebanisterialopez.ebanisterialopez.data.repository.SalesRepositoryImpl
import com.ebanisterialopez.ebanisterialopez.domain.repository.SalesRepository
import com.ebanisterialopez.ebanisterialopez.domain.usecase.CrearVentaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://ebanisterialopez-api-gkech8c3f5f2f0eq.centralus-01.azurewebsites.net/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(client: OkHttpClient): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductApi(client: OkHttpClient): ProductApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSalesApi(client: OkHttpClient): SalesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SalesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVentaRemoteDataSource(api: SalesApi): VentaRemoteDataSource {
        return VentaRemoteDataSource(api)
    }
    @Provides
    @Singleton
    fun provideSalesRepository(remoteDataSource: VentaRemoteDataSource): SalesRepository {
        return SalesRepositoryImpl(remoteDataSource)
    }
    @Provides
    @Singleton
    fun provideCrearVentaUseCase(
        repository: SalesRepository,
        carritoLocalDataSource: CarritoLocalDataSource
    ): CrearVentaUseCase {
        return CrearVentaUseCase(repository, carritoLocalDataSource)
    }
}