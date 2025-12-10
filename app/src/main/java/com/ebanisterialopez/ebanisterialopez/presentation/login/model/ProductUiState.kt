package com.ebanisterialopez.ebanisterialopez.presentation.login.model



import com.ebanisterialopez.ebanisterialopez.domain.model.Product

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val message: String) : ProductUiState()
}