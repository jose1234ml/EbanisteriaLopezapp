package com.ebanisterialopez.ebanisterialopez.presentation.model

import com.ebanisterialopez.ebanisterialopez.domain.model.Product
sealed interface CategoryProductsState {
    data object Loading : CategoryProductsState
    data class Success(val products: List<Product>) : CategoryProductsState
    data class Error(val message: String) : CategoryProductsState
}