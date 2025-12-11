package com.ebanisterialopez.ebanisterialopez.data.remote.datasource
import com.ebanisterialopez.ebanisterialopez.data.remote.ProductApi
import javax.inject.Inject

interface OrderRemoteDataSource {
    suspend fun sendOrderToApi(orderData: Map<String, Any?>): Boolean
}

class OrderRemoteDataSourceImpl @Inject constructor(
    private val productApi: ProductApi
) : OrderRemoteDataSource {
    override suspend fun sendOrderToApi(orderData: Map<String, Any?>): Boolean {
        println(" [RemoteDataSource] Orden enviada a la API: $orderData")
        return true
    }
}