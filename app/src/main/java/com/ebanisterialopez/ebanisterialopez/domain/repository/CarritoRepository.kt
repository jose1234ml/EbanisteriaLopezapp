package com.ebanisterialopez.ebanisterialopez.domain.repository

import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun getCartItems(): Flow<List<CarritoItem>>
    suspend fun addItem(item: CarritoItem)
    suspend fun updateItem(item: CarritoItem)
    suspend fun removeItem(item: CarritoItem)
    suspend fun clearCart()
}