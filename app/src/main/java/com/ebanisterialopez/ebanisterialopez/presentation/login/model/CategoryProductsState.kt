package com.ebanisterialopez.ebanisterialopez.presentation.login.model


import com.ebanisterialopez.ebanisterialopez.domain.model.Product

/**
 * Define los posibles estados de la UI para la pantalla de productos por categoría.
 */
sealed interface CategoryProductsState {
    data object Loading : CategoryProductsState
    data class Success(val products: List<Product>) : CategoryProductsState
    data class Error(val message: String) : CategoryProductsState
}