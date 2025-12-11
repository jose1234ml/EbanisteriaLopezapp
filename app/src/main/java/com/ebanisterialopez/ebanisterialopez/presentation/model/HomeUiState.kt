package com.ebanisterialopez.ebanisterialopez.presentation.model

import com.ebanisterialopez.ebanisterialopez.domain.model.Product

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null
)