package com.ebanisterialopez.ebanisterialopez.presentation.cotizacion

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ebanisterialopez.ebanisterialopez.presentation.home.CotizacionViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.model.CotizacionIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    navController: NavController,
    viewModel: CotizacionViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

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
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onIntent(CotizacionIntent.UpdateNombre(it)) },
                label = { Text("Tu Nombre Completo *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = state.telefono,
                onValueChange = { viewModel.onIntent(CotizacionIntent.UpdateTelefono(it)) },
                label = { Text("Teléfono de Contacto *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onIntent(CotizacionIntent.UpdateEmail(it)) },
                label = { Text("Correo Electrónico (Opcional)") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = state.producto,
                onValueChange = { viewModel.onIntent(CotizacionIntent.UpdateProducto(it)) },
                label = { Text("Producto o Servicio de Interés *") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = state.detalles,
                onValueChange = { viewModel.onIntent(CotizacionIntent.UpdateDetalles(it)) },
                label = { Text("Detalles del Proyecto / Medidas *") },
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(bottom = 20.dp),
                minLines = 5
            )

            Button(
                onClick = { viewModel.onIntent(CotizacionIntent.SendQuote, context) },
                enabled = state.isFormValid && !state.isSending,
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Icon(Icons.Filled.Send, contentDescription = "Enviar")
                Spacer(Modifier.width(8.dp))
                Text(if (state.isSending) "Enviando..." else "Enviar por WhatsApp")
            }
        }
    }
}
