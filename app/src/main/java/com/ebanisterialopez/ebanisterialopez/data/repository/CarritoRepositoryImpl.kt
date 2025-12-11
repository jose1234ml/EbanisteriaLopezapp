package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class CarritoRepositoryImpl @Inject constructor(
    private val dao: CarritoDao
) : CarritoRepository {

    override fun getCartItems(): Flow<List<CarritoItem>> {
        return dao.getCartFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addItem(item: CarritoItem) = dao.addItem(item.toEntity())
    override suspend fun updateItem(item: CarritoItem) = dao.updateItem(item.toEntity())
    override suspend fun removeItem(item: CarritoItem) = dao.removeItem(item.toEntity())
    override suspend fun clearCart() = dao.clearCart()

    private fun CarritoItemEntity.toDomain() = CarritoItem(
        id = id,
        productoId = productoId,
        name = name,
        price = price,
        quantity = quantity,
        imageUrl = imageUrl
    )

    private fun CarritoItem.toEntity() = CarritoItemEntity(
        id = id,
        productoId = productoId,
        name = name,
        price = price,
        quantity = quantity,
        imageUrl = imageUrl
    )
}