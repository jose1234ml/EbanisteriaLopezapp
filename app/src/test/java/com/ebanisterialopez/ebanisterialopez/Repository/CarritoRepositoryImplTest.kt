package com.ebanisterialopez.ebanisterialopez.Repository

import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.repository.CarritoRepositoryImpl
import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CarritoRepositoryImplTest {

    private lateinit var dao: CarritoDao
    private lateinit var repository: CarritoRepositoryImpl

    private val testEntity = CarritoItemEntity(
        id = 1,
        productoId = 1,
        name = "Mesa",
        price = "1000",
        quantity = 2,
        imageUrl = "url"
    )

    private val testDomain = CarritoItem(
        id = 1,
        productoId = 1,
        name = "Mesa",
        price = "1000",
        quantity = 2,
        imageUrl = "url"
    )

    @Before
    fun setup() {
        dao = mock()
        repository = CarritoRepositoryImpl(dao)
    }

    @Test
    fun `getCartItems retorna lista correcta`() = runTest {
        whenever(dao.getCartFlow()).thenReturn(flow { emit(listOf(testEntity)) })

        val items = repository.getCartItems()
        items.collect { list ->
            assert(list.size == 1)
            assert(list[0] == testDomain)
        }
    }

    @Test
    fun `addItem llama a dao con entity`() = runTest {
        repository.addItem(testDomain)
        verify(dao).addItem(testEntity)
    }

    @Test
    fun `updateItem llama a dao con entity`() = runTest {
        repository.updateItem(testDomain)
        verify(dao).updateItem(testEntity)
    }

    @Test
    fun `removeItem llama a dao con entity`() = runTest {
        repository.removeItem(testDomain)
        verify(dao).removeItem(testEntity)
    }

    @Test
    fun `clearCart llama a dao`() = runTest {
        repository.clearCart()
        verify(dao).clearCart()
    }
}