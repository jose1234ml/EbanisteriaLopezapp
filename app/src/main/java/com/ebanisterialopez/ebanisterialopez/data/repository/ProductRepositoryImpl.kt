package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.ProductRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductCategory
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun getFeaturedProducts(): List<ProductApiData> =
        remoteDataSource.getFeaturedProducts()

    override suspend fun getProductById(id: Int): ProductApiData? =
        remoteDataSource.getProductById(id)

    override suspend fun getProductsByCategory(category: String): List<ProductApiData> =
        remoteDataSource.getProductsByCategory(category)

    override suspend fun getCategories(): List<ProductCategory> =
        remoteDataSource.getCategories()
}