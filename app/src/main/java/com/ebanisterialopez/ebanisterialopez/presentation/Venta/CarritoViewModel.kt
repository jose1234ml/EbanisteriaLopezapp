package com.ebanisterialopez.ebanisterialopez.presentation.Venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repo: CarritoRepository
) : ViewModel() {
    val items: StateFlow<List<CarritoItemEntity>> = repo.getCartFlow()
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, emptyList())
    fun addItem(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val item = CarritoItemEntity(
                productoId = product.productoId,
                name = product.name,
                price = product.price,
                quantity = quantity,
                imageUrl = product.imageUrl
            )
            repo.addItem(item)
        }
    }
    fun updateItem(item: CarritoItemEntity) {
        viewModelScope.launch { repo.updateItem(item) }
    }
    fun removeItem(item: CarritoItemEntity) {
        viewModelScope.launch { repo.removeItem(item) }
    }
    fun clearCart() {
        viewModelScope.launch { repo.clearCart() }
    }
}