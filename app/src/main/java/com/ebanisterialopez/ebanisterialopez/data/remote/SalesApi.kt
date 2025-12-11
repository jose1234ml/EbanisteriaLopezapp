package com.ebanisterialopez.ebanisterialopez.data.remote

import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SalesApi {
    @POST("api/Ventas")
    suspend fun crearVenta(@Body venta: CrearVentaDto): Response<Void>
}