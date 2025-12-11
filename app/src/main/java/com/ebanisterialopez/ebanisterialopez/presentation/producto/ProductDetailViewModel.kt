package com.ebanisterialopez.ebanisterialopez.presentation.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.ProductApiData
import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import com.ebanisterialopez.ebanisterialopez.presentation.model.ProductDetailIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.ProductDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val carritoRepository: CarritoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    init {
        val productId = savedStateHandle.get<String>("productId")?.toIntOrNull()
        if (productId != null) {
            onIntent(ProductDetailIntent.LoadProduct(productId))
        } else {
            _state.update { it.copy(errorMessage = "ID de producto no válido") }
        }
    }

    fun onIntent(intent: ProductDetailIntent) {
        when (intent) {
            is ProductDetailIntent.LoadProduct -> loadProduct(intent.productId)
            is ProductDetailIntent.AddToCart -> addToCart(intent.product, intent.quantity)
        }
    }

    private fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val apiData: ProductApiData? = repository.getProductById(productId)
                if (apiData != null) {
                    val product = apiData.toDomain()
                    _state.update { it.copy(isLoading = false, product = product) }
                } else {
                    _state.update { it.copy(isLoading = false, errorMessage = "Producto no encontrado") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = "Error al cargar: ${e.message}") }
            }
        }
    }

    private fun addToCart(product: Product, quantity: Int) {
        viewModelScope.launch {
            val item = CarritoItem(
                productoId = product.productoId,
                name = product.name,
                price = product.price,
                quantity = quantity,
                imageUrl = product.imageUrl
            )
            carritoRepository.addItem(item)
        }
    }

    private fun ProductApiData.toDomain(): Product {
        return Product(
            productoId = productoId,
            name = nombre ?: "",
            price = precio?.toString() ?: "0",
            color = color ?: "",
            material = material ?: "",
            dimensiones = dimensiones ?: "",
            imageUrl = imagenes?.firstOrNull()?.urlImagen ?: "",
            description = detalle?.descripcion ?: "",
            images = imagenes?.mapNotNull { it.urlImagen } ?: emptyList()
        )
    }
}