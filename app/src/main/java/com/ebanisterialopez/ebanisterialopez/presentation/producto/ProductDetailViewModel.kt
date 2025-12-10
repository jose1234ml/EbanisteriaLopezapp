package com.ebanisterialopez.ebanisterialopez.presentation.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.toProductUiModel
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.domain.repository.CarritoRepository
import com.ebanisterialopez.ebanisterialopez.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
sealed class ProductDetailState {
    object Loading : ProductDetailState()
    data class Success(val product: Product) : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
}
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val carritoRepository: CarritoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val uiState: StateFlow<ProductDetailState> = _uiState
    init {
        val productId = savedStateHandle.get<String>("productId")?.toIntOrNull()
        if (productId != null) {
            fetchProductDetails(productId)
        } else {
            _uiState.value = ProductDetailState.Error(
                "ID de producto no válido. Revisa la ruta de navegación: se esperaba 'productId'."
            )
        }
    }
    private fun fetchProductDetails(productId: Int) {
        viewModelScope.launch {
            _uiState.value = ProductDetailState.Loading
            try {
                val apiData = repository.getProductById(productId)
                if (apiData != null) {
                    val mappedProduct = apiData.toProductUiModel()
                    _uiState.value = ProductDetailState.Success(mappedProduct)
                } else {
                    _uiState.value = ProductDetailState.Error("Producto con ID $productId no encontrado.")
                }
            } catch (e: Exception) {
                _uiState.value = ProductDetailState.Error("Fallo al cargar el detalle: ${e.message}")
            }
        }
    }
    fun addToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            carritoRepository.addItem(
                CarritoItemEntity(
                    productoId = product.productoId,
                    name = product.name,
                    price = product.price,
                    quantity = quantity,
                    imageUrl = product.imageUrl
                )
            )
        }
    }
}