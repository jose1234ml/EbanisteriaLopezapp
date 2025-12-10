package com.ebanisterialopez.ebanisterialopez.presentation.cotizacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
const val WHATSAPP_NUMBER = "+18299236254"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(navController: NavController) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var productoInteres by remember { mutableStateOf("") }
    var detalles by remember { mutableStateOf("") }

    val isFormValid = nombre.isNotBlank() && telefono.isNotBlank() && productoInteres.isNotBlank() && detalles.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitar Cotización", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Dinos qué necesitas y te enviaremos una cotización detallada.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Tu Nombre Completo *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary)
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de Contacto *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico (Opcional)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary)
            )
            OutlinedTextField(
                value = productoInteres,
                onValueChange = { productoInteres = it },
                label = { Text("Producto o Servicio de Interés *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary)
            )
            OutlinedTextField(
                value = detalles,
                onValueChange = { detalles = it },
                label = { Text("Detalles del Proyecto / Medidas *") },
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(bottom = 20.dp),
                minLines = 5,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colors.primary)
            )

            Button(
                onClick = {
                    sendWhatsAppQuote(
                        context = context,
                        nombre = nombre,
                        telefono = telefono,
                        email = email,
                        producto = productoInteres,
                        detalles = detalles
                    )
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Enviar")
                Spacer(Modifier.width(8.dp))
                Text("Enviar Solicitud por WhatsApp", fontWeight = FontWeight.Bold)
            }
        }
    }
}
fun sendWhatsAppQuote(
    context: Context,
    nombre: String,
    telefono: String,
    email: String,
    producto: String,
    detalles: String
) {
    val message = """
        *SOLICITUD DE COTIZACIÓN*
        
        *Cliente:* $nombre
        *Teléfono:* $telefono
        *Email:* ${if (email.isBlank()) "No Proporcionado" else email}
        
        *Producto/Servicio:* $producto
        
        *Detalles/Especificaciones:*
        $detalles
        
        ---
        _Enviado desde la App Móvil_
    """.trimIndent()

    try {
        val baseUri = Uri.parse("https://wa.me/$WHATSAPP_NUMBER?text=" + Uri.encode(message))
        val businessIntent = Intent(Intent.ACTION_VIEW)
        businessIntent.data = baseUri
        businessIntent.setPackage("com.whatsapp.w4b")

        if (businessIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(businessIntent)
            return
        }

        val standardIntent = Intent(Intent.ACTION_VIEW)
        standardIntent.data = baseUri
        standardIntent.setPackage("com.whatsapp")
        if (standardIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(standardIntent)
            return
        }

        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = baseUri

        if (browserIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(browserIntent)
        } else {
            Toast.makeText(context, "No se encontró WhatsApp ni una aplicación para manejar el enlace.", Toast.LENGTH_LONG).show()
        }

    } catch (e: Exception) {
        Toast.makeText(context, "Error fatal al intentar abrir el enlace: ${e.message}", Toast.LENGTH_LONG).show()
    }
}