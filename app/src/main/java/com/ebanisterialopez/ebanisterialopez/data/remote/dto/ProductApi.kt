package com.ebanisterialopez.ebanisterialopez.data.remote.dto

import retrofit2.http.GET
import retrofit2.http.Path
interface ProductApi {
    @GET("api/productos")
    suspend fun getFeaturedProducts(): List<ProductApiData>
    @GET("api/productos/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductApiData?
    @GET("api/productos/categoria/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<ProductApiData>
    @GET("api/categorias")
    suspend fun getCategories(): List<ProductCategory>
}