package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.repository.mapper.toDtoFromCarrito
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import com.ebanisterialopez.ebanisterialopez.domain.repository.SalesRepository
import javax.inject.Inject
class CrearVentaUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(
        venta: Venta,
        carritoItems: List<CarritoItemEntity>
    ): Result<Unit> {
        val dto = venta.toDtoFromCarrito(carritoItems)
        return repository.crearVenta(dto)
    }
}