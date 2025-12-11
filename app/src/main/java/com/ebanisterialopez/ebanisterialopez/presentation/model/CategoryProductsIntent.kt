package com.ebanisterialopez.ebanisterialopez.presentation.model


sealed class CategoryProductsIntent {
    data class LoadProducts(val category: String) : CategoryProductsIntent()
}