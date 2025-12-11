package com.ebanisterialopez.ebanisterialopez.presentation.Venta

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import com.ebanisterialopez.ebanisterialopez.domain.usecase.CrearVentaUseCase
import com.ebanisterialopez.ebanisterialopez.presentation.model.CheckoutIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val crearVentaUseCase: CrearVentaUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CheckoutState())
    val state: State<CheckoutState> get() = _state

    fun onIntent(intent: CheckoutIntent) {
        when (intent) {
            is CheckoutIntent.ConfirmarPedido -> confirmarPedido()
            is CheckoutIntent.ClearErrorMessage -> reduce { it.copy(errorMessage = null) }
            is CheckoutIntent.UpdateClienteInfo -> reduce {
                it.copy(
                    nombreCliente = intent.nombre,
                    telefono = intent.telefono,
                    direccion = intent.direccion
                )
            }
            is CheckoutIntent.UpdatePaymentMethod -> reduce { it.copy(metodoPago = intent.metodo) }
            is CheckoutIntent.UploadComprobante -> reduce { it.copy(comprobanteUri = intent.uri) }
        }
    }

    private fun confirmarPedido() {
        val current = _state.value
        if (current.nombreCliente.isBlank() || current.telefono.isBlank() || current.direccion.isBlank()) {
            reduce { it.copy(errorMessage = "Completa todos los campos") }
            return
        }

        reduce { it.copy(isLoading = true, errorMessage = null, success = false) }

        viewModelScope.launch {
            crearVentaUseCase(
                Venta(
                    nombreCliente = current.nombreCliente,
                    telefono = current.telefono,
                    direccion = current.direccion,
                    metodoPago = current.metodoPago,
                    comprobanteUri = current.comprobanteUri
                )
            ).collect { result ->
                when (result) {
                    is CrearVentaUseCase.Result.Success -> reduce { it.copy(isLoading = false, success = true) }
                    is CrearVentaUseCase.Result.Error -> reduce { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    private inline fun reduce(block: (CheckoutState) -> CheckoutState) {
        _state.value = block(_state.value)
    }
}