package com.ebanisterialopez.ebanisterialopez.presentation.model

import com.ebanisterialopez.ebanisterialopez.domain.model.Product

sealed class ProductDetailIntent {
    data class LoadProduct(val productId: Int) : ProductDetailIntent()
    data class AddToCart(val product: Product, val quantity: Int = 1) : ProductDetailIntent()
}