package com.ebanisterialopez.ebanisterialopez.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carrito_items")
data class CarritoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productoId: Int,
    val name: String,
    val price: String,
    val quantity: Int,
    val imageUrl: String
)