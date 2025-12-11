package com.ebanisterialopez.ebanisterialopez.presentation.model

import com.ebanisterialopez.ebanisterialopez.domain.model.Product

data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null
)