package com.ebanisterialopez.ebanisterialopez.domain.model

data class CarritoItem(
    val id: Int = 0,
    val productoId: Int,
    val name: String,
    val price: String,
    val quantity: Int,
    val imageUrl: String
)