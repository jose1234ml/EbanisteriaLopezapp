package com.ebanisterialopez.ebanisterialopez.presentation.model


import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem

data class CarritoState(
    val items: List<CarritoItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val total: Double = 0.0
)