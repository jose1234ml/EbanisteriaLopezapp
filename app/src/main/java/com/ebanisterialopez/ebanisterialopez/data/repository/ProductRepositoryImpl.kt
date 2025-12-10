package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApi
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductCategory
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import retrofit2.HttpException
import javax.inject.Inject
class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getFeaturedProducts(): List<ProductApiData> {
        return try {
            api.getFeaturedProducts()
        } catch (e: HttpException) {
            throw Exception("Error HTTP ${e.code()}: ${e.message()}", e)
        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getProductById(id: Int): ProductApiData? {
        return try {
            api.getProductById(id)
        } catch (e: HttpException) {
            throw Exception("Error HTTP ${e.code()}: ${e.message()}", e)
        } catch (e: Exception) {
            throw e
        }
    }

    // Firma CORRECTA: retorna List<ProductApiData>
    override suspend fun getProductsByCategory(category: String): List<ProductApiData> {
        return try {
            api.getProductsByCategory(category)
        } catch (e: HttpException) {
            throw Exception("Error HTTP ${e.code()}: ${e.message()}", e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCategories(): List<ProductCategory> {
        return try {
            api.getCategories()
        } catch (e: HttpException) {
            throw Exception("Error HTTP ${e.code()}: ${e.message()}", e)
        } catch (e: Exception) {
            throw e
        }
    }
}