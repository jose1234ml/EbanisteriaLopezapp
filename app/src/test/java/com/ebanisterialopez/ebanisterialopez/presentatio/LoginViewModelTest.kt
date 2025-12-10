package com.ebanisterialopez.ebanisterialopez.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ebanisterialopez.ebanisterialopez.data.remote.util.Resource
import com.ebanisterialopez.ebanisterialopez.domain.model.AuthToken
import com.ebanisterialopez.ebanisterialopez.domain.usecase.LoginUseCase
import com.ebanisterialopez.ebanisterialopez.presentation.model.LoginUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private lateinit var mockLoginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockLoginUseCase = mockk()
        viewModel = LoginViewModel(mockLoginUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onLoginClick when useCase returns Success should set isAuthenticated to true and update token`() = testScope.runTest {
        val testEmail = "test@example.com"
        val testPassword = "password123"
        val testToken = "abc-123-xyz"
        val authTokenSuccess = AuthToken(token = testToken, userId = 123)

        coEvery { mockLoginUseCase(testEmail, testPassword) } returns Resource.Success(authTokenSuccess)

        assertFalse(viewModel.state.first().isAuthenticated)

        viewModel.onLoginClick(testEmail, testPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.first()

        assertFalse(finalState.isLoading)
        assertTrue(finalState.isAuthenticated)
        assertEquals(testToken, finalState.authToken)
        assertEquals(testEmail, finalState.email)
        assertNull(finalState.error)
    }

    @Test
    fun `onLoginClick when useCase returns Error should set error message and set isLoading to false`() = testScope.runTest {
        val testEmail = "test@example.com"
        val testPassword = "wrongpassword"
        val errorMessage = "Credenciales inválidas"

        coEvery { mockLoginUseCase(testEmail, testPassword) } returns Resource.Error(errorMessage)

        viewModel.onLoginClick(testEmail, testPassword)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.first()

        assertFalse(finalState.isLoading)
        assertFalse(finalState.isAuthenticated)
        assertEquals(errorMessage, finalState.error)
        assertNull(finalState.authToken)
    }

    @Test
    fun `logout should reset state to initial LoginUiState`() = testScope.runTest {

        viewModel.stateForTest.value = LoginUiState(
            isAuthenticated = true,
            authToken = "dummy_token",
            email = "logged@in.com"
        )

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.state.first()
        val initialState = LoginUiState()

        assertEquals(initialState.isAuthenticated, finalState.isAuthenticated)
        assertEquals(initialState.authToken, finalState.authToken)
        assertEquals(initialState, finalState)
    }
}