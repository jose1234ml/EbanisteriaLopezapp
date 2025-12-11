package com.ebanisterialopez.ebanisterialopez.data.remote.datasource

import com.ebanisterialopez.ebanisterialopez.data.remote.ProductApi
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductCategory
import javax.inject.Inject

interface ProductRemoteDataSource {
    suspend fun getFeaturedProducts(): List<ProductApiData>
    suspend fun getProductById(id: Int): ProductApiData?
    suspend fun getProductsByCategory(category: String): List<ProductApiData>
    suspend fun getCategories(): List<ProductCategory>
}

class ProductRemoteDataSourceImpl @Inject constructor(
    private val api: ProductApi
) : ProductRemoteDataSource {

    override suspend fun getFeaturedProducts(): List<ProductApiData> =
        try {
            api.getFeaturedProducts()
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getProductById(id: Int): ProductApiData? =
        try {
            api.getProductById(id)
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getProductsByCategory(category: String): List<ProductApiData> =
        try {
            api.getProductsByCategory(category)
        } catch (e: Exception) {
            throw e
        }

    override suspend fun getCategories(): List<ProductCategory> =
        try {
            api.getCategories()
        } catch (e: Exception) {
            throw e
        }
}