package com.ebanisterialopez.ebanisterialopez.presentation.model

data class CotizacionState(
    val nombre: String = "",
    val telefono: String = "",
    val email: String = "",
    val producto: String = "",
    val detalles: String = "",
    val isFormValid: Boolean = false,
    val errorMessage: String? = null,
    val isSending: Boolean = false
)