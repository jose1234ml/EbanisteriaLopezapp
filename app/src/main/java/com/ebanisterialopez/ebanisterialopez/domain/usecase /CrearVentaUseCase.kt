package com.ebanisterialopez.ebanisterialopez.domain.usecase

import com.ebanisterialopez.ebanisterialopez.data.datasource.local.CarritoLocalDataSource
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.repository.mapper.toDtoFromCarrito
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import com.ebanisterialopez.ebanisterialopez.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CrearVentaUseCase @Inject constructor(
    private val repository: SalesRepository,
    private val carritoLocalDataSource: CarritoLocalDataSource
) {
    sealed class Result {
        object Success : Result()
        data class Error(val message: String) : Result()
    }

    operator fun invoke(venta: Venta): Flow<Result> = flow {
        val carritoItems: List<CarritoItemEntity> = carritoLocalDataSource.getItems()
        if (carritoItems.isEmpty()) {
            emit(Result.Error("El carrito está vacío"))
            return@flow
        }
        try {
            val dto = venta.toDtoFromCarrito(carritoItems)
            repository.crearVenta(dto)
            emit(Result.Success)
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Error inesperado"))
        }
    }
}