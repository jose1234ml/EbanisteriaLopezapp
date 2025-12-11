package com.ebanisterialopez.ebanisterialopez.presentation.model


import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.domain.model.Product

sealed class CarritoIntent {
    data class AddItem(val product: Product, val quantity: Int = 1) : CarritoIntent()
    data class UpdateItem(val item: CarritoItem) : CarritoIntent()
    data class RemoveItem(val item: CarritoItem) : CarritoIntent()
    object ClearCart : CarritoIntent()
    data class SetItems(val items: List<CarritoItem>) : CarritoIntent()
}