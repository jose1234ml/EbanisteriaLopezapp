package com.ebanisterialopez.ebanisterialopez.presentation.model

import android.net.Uri

sealed class CheckoutIntent {
    object ConfirmarPedido : CheckoutIntent()
    object ClearErrorMessage : CheckoutIntent()
    data class UpdateClienteInfo(val nombre: String, val telefono: String, val direccion: String) : CheckoutIntent()
    data class UpdatePaymentMethod(val metodo: String) : CheckoutIntent()
    data class UploadComprobante(val uri: Uri) : CheckoutIntent()
}