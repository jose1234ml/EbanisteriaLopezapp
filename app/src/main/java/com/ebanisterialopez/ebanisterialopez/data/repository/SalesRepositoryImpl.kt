package com.ebanisterialopez.ebanisterialopez.data.remote.repository

import android.util.Log
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.CrearVentaDto
import com.ebanisterialopez.ebanisterialopez.data.remote.dto.SalesApi
import com.ebanisterialopez.ebanisterialopez.domain.repository.SalesRepository
import com.google.gson.Gson
import javax.inject.Inject
class SalesRepositoryImpl @Inject constructor(
    private val api: SalesApi
) : SalesRepository {

    override suspend fun crearVenta(dto: CrearVentaDto): Result<Unit> {
        return try {
            val gson = Gson()
            Log.i("SalesRepo", "Enviar crearVenta JSON: ${gson.toJson(dto)}")

            val response = api.crearVenta(dto)
            Log.i("SalesRepo", "HTTP code=${response.code()}, success=${response.isSuccessful}")

            response.body()?.let { Log.i("SalesRepo", "body: $it") }
            response.errorBody()?.let { err ->
                val errStr = try { err.string() } catch (e: Exception) { "errorBody read failed: ${e.message}" }
                Log.e("SalesRepo", "errorBody: $errStr")
            }

            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("HTTP ${response.code()} - revisar logs para errorBody"))
        } catch (e: Exception) {
            Log.e("SalesRepo", "Exception crearVenta: ${e.message}", e)
            Result.failure(e)
        }
    }
}