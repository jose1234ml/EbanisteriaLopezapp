package com.ebanisterialopez.ebanisterialopez.data.remote.datasource

import android.util.Log
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import com.ebanisterialopez.ebanisterialopez.data.remote.SalesApi
import com.google.gson.Gson
import javax.inject.Inject

class VentaRemoteDataSource @Inject constructor(
    private val api: SalesApi
) {
    suspend fun crearVenta(dto: CrearVentaDto) {
        val gson = Gson()
        Log.i("VentaRemoteDS", "Enviar crearVenta JSON: ${gson.toJson(dto)}")

        val response = api.crearVenta(dto)
        Log.i("VentaRemoteDS", "HTTP code=${response.code()}, success=${response.isSuccessful}")

        response.body()?.let { Log.i("VentaRemoteDS", "body: $it") }
        response.errorBody()?.let { err ->
            val errStr = try { err.string() } catch (e: Exception) { "errorBody read failed: ${e.message}" }
            Log.e("VentaRemoteDS", "errorBody: $errStr")
        }

        if (!response.isSuccessful) {
            throw Exception("HTTP ${response.code()} - revisar logs para errorBody")
        }
    }
}