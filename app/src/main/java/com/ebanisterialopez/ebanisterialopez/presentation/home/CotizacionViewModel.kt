package com.ebanisterialopez.ebanisterialopez.presentation.home


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebanisterialopez.ebanisterialopez.presentation.model.CotizacionIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.CotizacionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val WHATSAPP_NUMBER = "+18299236254"

@HiltViewModel
class CotizacionViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(CotizacionState())
    val uiState: StateFlow<CotizacionState> = _uiState.asStateFlow()

    fun onIntent(intent: CotizacionIntent, context: Context? = null) {
        when(intent) {
            is CotizacionIntent.UpdateNombre -> updateState(nombre = intent.value)
            is CotizacionIntent.UpdateTelefono -> updateState(telefono = intent.value)
            is CotizacionIntent.UpdateEmail -> updateState(email = intent.value)
            is CotizacionIntent.UpdateProducto -> updateState(producto = intent.value)
            is CotizacionIntent.UpdateDetalles -> updateState(detalles = intent.value)
            CotizacionIntent.SendQuote -> context?.let { sendQuote(it) }
        }
    }

    private fun updateState(
        nombre: String = _uiState.value.nombre,
        telefono: String = _uiState.value.telefono,
        email: String = _uiState.value.email,
        producto: String = _uiState.value.producto,
        detalles: String = _uiState.value.detalles
    ) {
        _uiState.value = _uiState.value.copy(
            nombre = nombre,
            telefono = telefono,
            email = email,
            producto = producto,
            detalles = detalles,
            isFormValid = nombre.isNotBlank() && telefono.isNotBlank() &&
                    producto.isNotBlank() && detalles.isNotBlank()
        )
    }

    private fun sendQuote(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSending = true)
            try {
                val message = """
                    *SOLICITUD DE COTIZACIÓN*
                    
                    *Cliente:* ${_uiState.value.nombre}
                    *Teléfono:* ${_uiState.value.telefono}
                    *Email:* ${if (_uiState.value.email.isBlank()) "No Proporcionado" else _uiState.value.email}
                    
                    *Producto/Servicio:* ${_uiState.value.producto}
                    
                    *Detalles/Especificaciones:*
                    ${_uiState.value.detalles}
         
                    _Enviado desde la App Móvil_
                """.trimIndent()

                val baseUri = Uri.parse("https://wa.me/$WHATSAPP_NUMBER?text=" + Uri.encode(message))
                val intent = Intent(Intent.ACTION_VIEW, baseUri)
                intent.setPackage("com.whatsapp.w4b")
                if (intent.resolveActivity(context.packageManager) == null) {
                    intent.setPackage("com.whatsapp")
                    if (intent.resolveActivity(context.packageManager) == null) {
                        intent.setPackage(null)
                    }
                }

                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error al enviar: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                _uiState.value = _uiState.value.copy(isSending = false)
            }
        }
    }
}