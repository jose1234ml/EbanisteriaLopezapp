package com.ebanisterialopez.ebanisterialopez.presentation.Venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import com.ebanisterialopez.ebanisterialopez.presentation.model.CarritoIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.CarritoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.NumberFormat
import java.util.Locale

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repo: CarritoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CarritoState())
    val state: StateFlow<CarritoState> = _state

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            repo.getCartItems().collect { items ->
                onIntent(CarritoIntent.SetItems(items))
            }
        }
    }

    fun onIntent(intent: CarritoIntent) {
        when (intent) {
            is CarritoIntent.AddItem -> addItem(intent.product, intent.quantity)
            is CarritoIntent.UpdateItem -> updateItem(intent.item)
            is CarritoIntent.RemoveItem -> removeItem(intent.item)
            is CarritoIntent.ClearCart -> clearCart()
            is CarritoIntent.SetItems -> reduce {
                it.copy(
                    items = intent.items,
                    total = calculateTotal(intent.items)
                )
            }
        }
    }

    private fun addItem(product: Product, quantity: Int) {
        viewModelScope.launch {
            try {
                repo.addItem(
                    CarritoItem(
                        productoId = product.productoId,
                        name = product.name,
                        price = product.price,
                        quantity = quantity,
                        imageUrl = product.imageUrl
                    )
                )
            } catch (e: Exception) {
                reduce { it.copy(errorMessage = e.message) }
            }
        }
    }

    private fun updateItem(item: CarritoItem) {
        viewModelScope.launch {
            try {
                repo.updateItem(item)
            } catch (e: Exception) {
                reduce { it.copy(errorMessage = e.message) }
            }
        }
    }

    private fun removeItem(item: CarritoItem) {
        viewModelScope.launch {
            try {
                repo.removeItem(item)
            } catch (e: Exception) {
                reduce { it.copy(errorMessage = e.message) }
            }
        }
    }

    private fun clearCart() {
        viewModelScope.launch {
            try {
                repo.clearCart()
            } catch (e: Exception) {
                reduce { it.copy(errorMessage = e.message) }
            }
        }
    }

    private fun calculateTotal(items: List<CarritoItem>): Double {
        return items.sumOf { item ->
            val cleanPrice = item.price.replace("RD$", "")
                .replace(",", "")
                .trim()
            cleanPrice.toDouble() * item.quantity.toDouble()
        }
    }

    private inline fun reduce(block: (CarritoState) -> CarritoState) {
        _state.value = block(_state.value)
    }
}