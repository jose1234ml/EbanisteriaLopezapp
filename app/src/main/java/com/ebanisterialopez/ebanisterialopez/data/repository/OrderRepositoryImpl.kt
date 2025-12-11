package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.OrderRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource
) : OrderRepository {

    override suspend fun submitOrder(orderData: Map<String, Any?>): Boolean {
        return remoteDataSource.sendOrderToApi(orderData)
    }
}