package com.ebanisterialopez.ebanisterialopez.data.remote.dto

import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
fun ProductApiData.toProductUiModel(): Product {
    val locale = Locale("es", "DO")
    val formatter = NumberFormat.getCurrencyInstance(locale)
    try { formatter.currency = Currency.getInstance("DOP") } catch (_: Exception) {}

    val priceValue = this.precio ?: 0
    val priceText = formatter.format(priceValue)
        .replace("DOP", "RD$")
        .replace(".00", "")
        .trim()

    val primaryImageUrl = this.imagenes?.firstOrNull()?.urlImagen
        ?: "https://placehold.co/400x300/CCCCCC/000000?text=No+Image"

    val imagesList = this.imagenes?.mapNotNull { it.urlImagen } ?: listOf(primaryImageUrl)

    val productDescription = this.detalle?.descripcion ?: this.nombre ?: "Sin descripción."

    return Product(
        productoId = this.productoId,
        name = this.nombre ?: "Producto Desconocido",
        description = productDescription,
        price = priceText,
        imageUrl = primaryImageUrl,
        material = this.material,
        color = this.color,
        dimensiones = this.dimensiones,
        images = imagesList
    )
}
fun ProductApiData.toDomain(): Product = this.toProductUiModel()