package com.ebanisterialopez.ebanisterialopez.domain.repository
interface OrderRepository {
    suspend fun submitOrder(orderData: Map<String, Any?>): Boolean
}