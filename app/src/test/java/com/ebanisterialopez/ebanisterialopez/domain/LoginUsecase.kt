package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    private lateinit var mockAuthRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        mockAuthRepository = mockk()
        loginUseCase = LoginUseCase(mockAuthRepository)
    }

    @Test
    fun `invoke with blank email returns Resource Error for empty email`() = runTest {
        val email = ""
        val password = "validpassword"

        val result = loginUseCase(email, password)

        assertTrue(result is Resource.Error)
        assertEquals("Email y contraseña no pueden estar vacíos.", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with blank password returns Resource Error for empty password`() = runTest {
        val email = "valid@email.com"
        val password = ""

        val result = loginUseCase(email, password)

        assertTrue(result is Resource.Error)
        assertEquals("Email y contraseña no pueden estar vacíos.", (result as Resource.Error).message)
    }

    @Test
    fun `invoke successful login returns Resource Success with AuthToken`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        // CORRECCIÓN: Se cambia el tipo de 'userId' de String ("u123") a Int (123)
        val expectedToken = AuthToken(token = "abc-123-xyz", userId = 123)

        coEvery { mockAuthRepository.login(email, password) } returns expectedToken

        val result = loginUseCase(email, password)

        assertTrue(result is Resource.Success)
        assertEquals(expectedToken.token, (result as Resource.Success).data?.token)
        assertEquals(123, result.data?.userId)
    }

    @Test
    fun `invoke repository throws exception returns Resource Error with custom message`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val exceptionMessage = "HTTP 500 Server Error"

        coEvery { mockAuthRepository.login(email, password) } throws IOException(exceptionMessage)

        val result = loginUseCase(email, password)

        assertTrue(result is Resource.Error)
        val errorMessage = (result as Resource.Error).message
        assertTrue(errorMessage?.contains("Error de login") == true)
        assertTrue(errorMessage?.contains(exceptionMessage) == true)
    }
}