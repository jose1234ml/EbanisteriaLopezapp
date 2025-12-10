package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApi // Usar tu API
import com.ebanisterialopez.ebanisterialopez.domain.repository.OrderRepository
import javax.inject.Inject
import kotlinx.coroutines.delay
class OrderRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : OrderRepository {

    override suspend fun submitOrder(orderData: Map<String, Any?>): Boolean {

        try {
            delay(1000)
            println(" [OrderRepository] Orden procesada y enviada a la API.")
            return true

        } catch (e: Exception) {
            println(" [OrderRepository] Error al enviar la orden a la API: ${e.message}")
            throw e
        }
    }
}