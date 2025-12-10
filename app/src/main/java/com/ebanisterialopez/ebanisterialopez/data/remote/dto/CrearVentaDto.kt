package com.ebanisterialopez.ebanisterialopez.data.remote.dto
data class CrearVentaItemDto(
    val productoId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)
data class CrearVentaDto(
    val correoUsuario: String? = null,
    val nombreCliente: String,
    val telefono: String,
    val direccion: String,
    val metodoPago: String,
    val urlVoucher: String?,
    val items: List<CrearVentaItemDto> = emptyList()
)