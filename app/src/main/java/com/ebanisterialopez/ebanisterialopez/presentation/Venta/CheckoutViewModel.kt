package com.ebanisterialopez.ebanisterialopez.presentation.Venta

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.dto.CarritoDao
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import com.ebanisterialopez.ebanisterialopez.domain.usecase.CrearVentaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val crearVentaUseCase: CrearVentaUseCase,
    private val carritoDao: CarritoDao
) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    fun confirmarPedido(venta: Venta) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val carritoItems: List<CarritoItemEntity> = carritoDao.getCartFlow().first()
                Log.i("CheckoutVM", "Carrito items count = ${carritoItems.size}")
                carritoItems.forEach { Log.i("CheckoutVM", "Cart item = $it") }
                if (carritoItems.isEmpty()) {
                    isLoading = false
                    errorMessage = "El carrito está vacío. Agrega productos antes de confirmar."
                    return@launch
                }
                val result = crearVentaUseCase(venta, carritoItems)
                isLoading = false
                result.onSuccess {
                }.onFailure { error ->
                    errorMessage = error.message ?: "Error al crear la venta"
                }
            } catch (e: Exception) {
                isLoading = false
                errorMessage = e.message ?: "Error inesperado"
                Log.e("CheckoutVM", "confirmarPedido exception: ${e.message}", e)
            }
        }
    }
    fun clearErrorMessage() {
        errorMessage = null
    }
}