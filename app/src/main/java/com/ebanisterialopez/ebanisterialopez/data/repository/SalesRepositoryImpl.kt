package com.ebanisterialopez.ebanisterialopez.data.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.datasource.VentaRemoteDataSource
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import com.ebanisterialopez.ebanisterialopez.domain.repository.SalesRepository
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val remoteDataSource: VentaRemoteDataSource
) : SalesRepository {

    override suspend fun crearVenta(venta: CrearVentaDto): Result<Unit> {
        return try {
            remoteDataSource.crearVenta(venta)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}