package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.AuthRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
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
class AuthRepositoryImplTest {

    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = AuthRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `login returns token when remoteDataSource succeeds`() = runTest {
        val email = "test@example.com"
        val password = "123456"
        val expectedToken = AuthToken(token = "dummy_token", userId = 1)

        coEvery { remoteDataSource.login(email, password) } returns expectedToken

        val result = repository.login(email, password)

        assertEquals(expectedToken, result)
        coVerify(exactly = 1) { remoteDataSource.login(email, password) }
    }

    @Test
    fun `login returns failure when remoteDataSource throws exception`() = runTest {
        val email = "test@example.com"
        val password = "123456"

        coEvery { remoteDataSource.login(email, password) } throws Exception("Error de red")

        val result = runCatching { repository.login(email, password) }

        assertTrue(result.isFailure)
        coVerify(exactly = 1) { remoteDataSource.login(email, password) }
    }

    @Test
    fun `register returns token when remoteDataSource succeeds`() = runTest {
        val email = "test@example.com"
        val password = "123456"
        val expectedToken = AuthToken(token = "dummy_register_token", userId = 2)

        coEvery { remoteDataSource.register(email, password) } returns expectedToken

        val result = repository.register(email, password)

        assertEquals(expectedToken, result)
        coVerify(exactly = 1) { remoteDataSource.register(email, password) }
    }

    @Test
    fun `register returns failure when remoteDataSource throws exception`() = runTest {
        val email = "test@example.com"
        val password = "123456"

        coEvery { remoteDataSource.register(email, password) } throws Exception("Error de red")

        val result = runCatching { repository.register(email, password) }

        assertTrue(result.isFailure)
        coVerify(exactly = 1) { remoteDataSource.register(email, password) }
    }
}