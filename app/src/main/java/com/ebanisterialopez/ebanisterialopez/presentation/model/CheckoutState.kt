package com.ebanisterialopez.ebanisterialopez.presentation.model

import android.net.Uri

data class CheckoutState(
    val nombreCliente: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val metodoPago: String = "Tarjeta de Crédito",
    val comprobanteUri: Uri? = null,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val errorMessage: String? = null
)