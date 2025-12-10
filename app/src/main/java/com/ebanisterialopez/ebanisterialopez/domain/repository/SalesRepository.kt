package com.ebanisterialopez.ebanisterialopez.domain.repository

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
interface SalesRepository {
    suspend fun crearVenta(venta: CrearVentaDto): Result<Unit>
}