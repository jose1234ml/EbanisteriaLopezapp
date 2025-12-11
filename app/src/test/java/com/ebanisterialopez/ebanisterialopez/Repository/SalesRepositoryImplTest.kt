package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.VentaRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SalesRepositoryImplTest {

    private lateinit var remoteDataSource: VentaRemoteDataSource
    private lateinit var repository: SalesRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = SalesRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `crearVenta returns success when remoteDataSource succeeds`() = runTest {
        val ventaDto = CrearVentaDto(
            nombreCliente = "Juan",
            telefono = "8091234567",
            direccion = "Santo Domingo",
            metodoPago = "Tarjeta",
            urlVoucher = null // Cambiado de comprobanteUri a urlVoucher
        )

        coEvery { remoteDataSource.crearVenta(ventaDto) } returns Unit

        val result = repository.crearVenta(ventaDto)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { remoteDataSource.crearVenta(ventaDto) }
    }

    @Test
    fun `crearVenta returns failure when remoteDataSource throws exception`() = runTest {
        val ventaDto = CrearVentaDto(
            nombreCliente = "Juan",
            telefono = "8091234567",
            direccion = "Santo Domingo",
            metodoPago = "Tarjeta",
            urlVoucher = null // Cambiado de comprobanteUri a urlVoucher
        )

        coEvery { remoteDataSource.crearVenta(ventaDto) } throws Exception("Error de red")

        val result = repository.crearVenta(ventaDto)

        assertTrue(result.isFailure)
        coVerify(exactly = 1) { remoteDataSource.crearVenta(ventaDto) }
    }
}