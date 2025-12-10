package com.ebanisterialopez.ebanisterialopez.domain.model

import android.net.Uri
data class Venta(
    val nombreCliente: String,
    val telefono: String,
    val direccion: String,
    val metodoPago: String,
    val comprobanteUri: Uri?
)


