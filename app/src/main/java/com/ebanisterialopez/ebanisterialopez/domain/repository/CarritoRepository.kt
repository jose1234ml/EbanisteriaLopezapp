package com.ebanisterialopez.ebanisterialopez.domain.repository

import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class CarritoRepository @Inject constructor(
    private val dao: CarritoDao
) {
    fun getCartFlow(): Flow<List<CarritoItemEntity>> = dao.getCartFlow()
    suspend fun addItem(item: CarritoItemEntity) = dao.addItem(item)
    suspend fun updateItem(item: CarritoItemEntity) = dao.updateItem(item)
    suspend fun removeItem(item: CarritoItemEntity) = dao.removeItem(item)
    suspend fun clearCart() = dao.clearCart()
}