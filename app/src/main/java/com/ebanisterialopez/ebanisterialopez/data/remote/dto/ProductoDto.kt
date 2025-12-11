package com.ebanisterialopez.ebanisterialopez.data.remote.dto

import kotlinx.serialization.Serializable
import com.ebanisterialopez.ebanisterialopez.domain.model.Product

@Serializable
data class ImagenProducto(
    val imagenId: Int? = null,
    val productoId: Int? = null,
    val urlImagen: String? = null,
    val orden: Int? = null,
    val producto: String? = null
)

@Serializable
data class ProductCategory(
    val categoriaId: Int? = null,
    val nombre: String? = null
)

@Serializable
data class ProductState(
    val estadoProductoId: Int? = null,
    val nombre: String? = null
)

@Serializable
data class ProductDetail(
    val productoDetalleId: Int? = null,
    val descripcion: String? = null,
    val material: String? = null,
    val color: String? = null,
    val dimensiones: String? = null,
    val productoId: Int? = null
)

@Serializable
data class ProductApiData(
    val productoId: Int,
    val nombre: String? = null,
    val color: String? = null,
    val material: String? = null,
    val dimensiones: String? = null,
    val precio: Int? = null,
    val categoriaId: Int? = null,
    val estadoProductoId: Int? = null,
    val categoria: ProductCategory? = null,
    val estadoProducto: ProductState? = null,
    val detalle: ProductDetail? = null,
    val imagenes: List<ImagenProducto>? = null,
) {
    fun ProductApiData.toProductDomain(): Product {
        return Product(
            productoId = productoId,
            name = nombre ?: "",
            price = precio?.toString() ?: "0",
            color = color ?: "",
            material = material ?: "",
            dimensiones = dimensiones ?: "",
            imageUrl = imagenes?.firstOrNull()?.urlImagen ?: "",
            description = detalle?.descripcion ?: "",
            images = imagenes?.mapNotNull { it.urlImagen } ?: emptyList()
        )
    }
}