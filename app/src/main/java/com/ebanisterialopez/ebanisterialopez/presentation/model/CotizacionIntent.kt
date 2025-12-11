package com.ebanisterialopez.ebanisterialopez.presentation.model


sealed class CotizacionIntent {
    data class UpdateNombre(val value: String): CotizacionIntent()
    data class UpdateTelefono(val value: String): CotizacionIntent()
    data class UpdateEmail(val value: String): CotizacionIntent()
    data class UpdateProducto(val value: String): CotizacionIntent()
    data class UpdateDetalles(val value: String): CotizacionIntent()
    object SendQuote: CotizacionIntent()
}