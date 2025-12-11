package com.ebanisterialopez.ebanisterialopez.Repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.ProductRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductCategory
import com.ebanisterialopez.ebanisterialopez.data.repository.ProductRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductRepositoryImplTest {

    private lateinit var remoteDataSource: ProductRemoteDataSource
    private lateinit var repository: ProductRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = ProductRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getFeaturedProducts returns list when remoteDataSource succeeds`() = runTest {
        val expectedProducts = listOf(
            ProductApiData(1, "Producto 1", "Descripción 1", "url1", ),
            ProductApiData(2, "Producto 2", "Descripción 2", "url2")
        )

        coEvery { remoteDataSource.getFeaturedProducts() } returns expectedProducts

        val result = repository.getFeaturedProducts()

        assertEquals(expectedProducts, result)
        coVerify(exactly = 1) { remoteDataSource.getFeaturedProducts() }
    }

    @Test
    fun `getProductById returns product when remoteDataSource succeeds`() = runTest {
        val product = ProductApiData(1, "Producto 1", "Descripción 1", "url1", )

        coEvery { remoteDataSource.getProductById(1) } returns product

        val result = repository.getProductById(1)

        assertEquals(product, result)
        coVerify(exactly = 1) { remoteDataSource.getProductById(1) }
    }

    @Test
    fun `getProductsByCategory returns list when remoteDataSource succeeds`() = runTest {
        val category = "Baño"
        val expectedProducts = listOf(
            ProductApiData(1, "Producto A", "Desc A", "urlA", ),
            ProductApiData(2, "Producto B", "Desc B", "urlB", )
        )

        coEvery { remoteDataSource.getProductsByCategory(category) } returns expectedProducts

        val result = repository.getProductsByCategory(category)

        assertEquals(expectedProducts, result)
        coVerify(exactly = 1) { remoteDataSource.getProductsByCategory(category) }
    }

    @Test
    fun `getCategories returns list when remoteDataSource succeeds`() = runTest {
        val expectedCategories = listOf(
            ProductCategory(),
            ProductCategory()
        )

        coEvery { remoteDataSource.getCategories() } returns expectedCategories

        val result = repository.getCategories()

        assertEquals(expectedCategories, result)
        coVerify(exactly = 1) { remoteDataSource.getCategories() }
    }

    @Test
    fun `methods return failure when remoteDataSource throws exception`() = runTest {
        coEvery { remoteDataSource.getFeaturedProducts() } throws Exception("Error API")
        coEvery { remoteDataSource.getProductById(1) } throws Exception("Error API")
        coEvery { remoteDataSource.getProductsByCategory("Baño") } throws Exception("Error API")
        coEvery { remoteDataSource.getCategories() } throws Exception("Error API")

        val featuredResult = runCatching { repository.getFeaturedProducts() }
        val productResult = runCatching { repository.getProductById(1) }
        val categoryProductsResult = runCatching { repository.getProductsByCategory("Baño") }
        val categoriesResult = runCatching { repository.getCategories() }

        assertTrue(featuredResult.isFailure)
        assertTrue(productResult.isFailure)
        assertTrue(categoryProductsResult.isFailure)
        assertTrue(categoriesResult.isFailure)

        coVerify(exactly = 1) { remoteDataSource.getFeaturedProducts() }
        coVerify(exactly = 1) { remoteDataSource.getProductById(1) }
        coVerify(exactly = 1) { remoteDataSource.getProductsByCategory("Baño") }
        coVerify(exactly = 1) { remoteDataSource.getCategories() }
    }
}