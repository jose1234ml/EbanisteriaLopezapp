package com.ebanisterialopez.ebanisterialopez.domain.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductCategory
interface ProductRepository {
    suspend fun getFeaturedProducts(): List<ProductApiData>
    suspend fun getProductById(id: Int): ProductApiData?
    suspend fun getProductsByCategory(category: String): List<ProductApiData>
    suspend fun getCategories(): List<ProductCategory>
}