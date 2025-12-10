package com.ebanisterialopez.ebanisterialopez.data.repository.mapper

import android.util.Base64
import com.ebanisterialopez.ebanisterialopez.MyApplication
import com.ebanisterialopez.ebanisterialopez.data.local.CarritoItemEntity
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaItemDto
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import java.text.NumberFormat
import java.util.Locale

/**
 * Convierte un CarritoItemEntity (Room) -> CrearVentaItemDto
 */
fun CarritoItemEntity.toCrearVentaItemDto(): CrearVentaItemDto {
    val precioDouble = parsePriceStringToDouble(this.price)
    return CrearVentaItemDto(
        productoId = this.productoId,
        cantidad = this.quantity,
        precioUnitario = precioDouble
    )
}

/**
 * Mapea Venta (dominio) + lista de items (Room) -> CrearVentaDto (para enviar a /api/Ventas)
 */
fun Venta.toCrearVentaDto(itemsCarrito: List<CarritoItemEntity>): CrearVentaDto {
    val base64Voucher = comprobanteUri?.let { uri ->
        val inputStream = MyApplication.appContext.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        bytes?.let { Base64.encodeToString(it, Base64.DEFAULT) }
    }

    val itemsDto = itemsCarrito.map { it.toCrearVentaItemDto() }

    return CrearVentaDto(
        correoUsuario = null,
        nombreCliente = this.nombreCliente,
        telefono = this.telefono,
        direccion = this.direccion,
        metodoPago = this.metodoPago,
        urlVoucher = base64Voucher,
        items = itemsDto
    )
}

/**
 * 🔥 FUNCIÓN QUE TE FALTABA 🔥
 * Hace exactamente lo que busca tu UseCase:
 * venta.toDtoFromCarrito(carritoItems)
 */
fun Venta.toDtoFromCarrito(itemsCarrito: List<CarritoItemEntity>): CrearVentaDto {
    return this.toCrearVentaDto(itemsCarrito)
}

/**
 * Parseo defensivo String -> Double
 */
private fun parsePriceStringToDouble(priceStr: String?): Double {
    if (priceStr.isNullOrBlank()) return 0.0
    val cleaned = priceStr
        .replace(Regex("[^0-9,\\.]"), "")
        .replace(",", ".")
        .trim()
    return try {
        cleaned.toDouble()
    } catch (e: Exception) {
        try {
            NumberFormat.getNumberInstance(Locale.getDefault()).parse(cleaned)?.toDouble() ?: 0.0
        } catch (_: Exception) {
            0.0
        }
    }
}