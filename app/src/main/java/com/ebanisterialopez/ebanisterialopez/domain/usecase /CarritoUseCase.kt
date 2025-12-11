package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    fun getCartItems(): Flow<List<CarritoItem>> {
        return repository.getCartItems()
    }
    suspend fun addItem(item: CarritoItem) = repository.addItem(item)
    suspend fun updateItem(item: CarritoItem) = repository.updateItem(item)
    suspend fun removeItem(item: CarritoItem) = repository.removeItem(item)
    suspend fun clearCart() = repository.clearCart()
}