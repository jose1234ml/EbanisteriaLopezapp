package com.ebanisterialopez.ebanisterialopez.Repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.OrderRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.repository.OrderRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderRepositoryImplTest {

    private lateinit var remoteDataSource: OrderRemoteDataSource
    private lateinit var repository: OrderRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = OrderRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `submitOrder returns true when remoteDataSource succeeds`() = runTest {
        val orderData = mapOf(
            "productId" to 1,
            "quantity" to 2,
            "customerName" to "Juan Perez"
        )

        coEvery { remoteDataSource.sendOrderToApi(orderData) } returns true

        val result = repository.submitOrder(orderData)

        assertTrue(result)
        coVerify(exactly = 1) { remoteDataSource.sendOrderToApi(orderData) }
    }

    @Test
    fun `submitOrder returns false when remoteDataSource throws exception`() = runTest {
        val orderData = mapOf(
            "productId" to 1,
            "quantity" to 2,
            "customerName" to "Juan Perez"
        )

        coEvery { remoteDataSource.sendOrderToApi(orderData) } throws Exception("API Error")

        val result = runCatching { repository.submitOrder(orderData) }

        assertTrue(result.isFailure)
        coVerify(exactly = 1) { remoteDataSource.sendOrderToApi(orderData) }
    }
}