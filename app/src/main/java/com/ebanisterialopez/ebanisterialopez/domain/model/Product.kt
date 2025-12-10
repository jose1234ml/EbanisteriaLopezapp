package com.ebanisterialopez.ebanisterialopez.domain.model

data class Product(
    val productoId: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val material: String?,
    val color: String?,
    val dimensiones: String?,
    val images: List<String>
)