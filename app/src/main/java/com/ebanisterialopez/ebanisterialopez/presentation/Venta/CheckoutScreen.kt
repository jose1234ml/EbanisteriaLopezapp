package com.ebanisterialopez.ebanisterialopez.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ebanisterialopez.ebanisterialopez.presentation.Venta.CheckoutViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.model.CheckoutIntent
import com.ebanisterialopez.ebanisterialopez.presentation.model.CheckoutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    orderTotal: String,
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val colors = MaterialTheme.colorScheme
    val state by viewModel.state
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { viewModel.onIntent(CheckoutIntent.UploadComprobante(it)) }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onIntent(CheckoutIntent.ClearErrorMessage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proceder a Pagar", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = colors.onSurface) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = colors.onSurface) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            CheckoutBottomBar(
                total = orderTotal,
                colors = colors,
                enabled = !state.isLoading
            ) {
                viewModel.onIntent(CheckoutIntent.ConfirmarPedido)
                onPlaceOrder()
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddressSection(state = state, viewModel = viewModel, colors = colors)
                Spacer(Modifier.height(16.dp))
                PaymentMethodSection(state = state, viewModel = viewModel, onUploadClick = { launcher.launch("image/*") }, colors = colors)
                Spacer(Modifier.height(16.dp))
                OrderSummarySection(subtotal = orderTotal, shipping = "Gratis", total = orderTotal, colors = colors)
                Spacer(Modifier.height(24.dp))
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(colors.scrim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        }
    }
}

@Composable
fun AddressSection(state: CheckoutState, viewModel: CheckoutViewModel, colors: ColorScheme) {
    CheckoutSection(title = "📦 Dirección de Envío", colors = colors) {
        OutlinedTextField(
            value = state.nombreCliente,
            onValueChange = { viewModel.onIntent(CheckoutIntent.UpdateClienteInfo(it, state.telefono, state.direccion)) },
            label = { Text("Nombre del Cliente") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colors.surface, unfocusedContainerColor = colors.surface)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.telefono,
            onValueChange = { viewModel.onIntent(CheckoutIntent.UpdateClienteInfo(state.nombreCliente, it, state.direccion)) },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colors.surface, unfocusedContainerColor = colors.surface)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.direccion,
            onValueChange = { viewModel.onIntent(CheckoutIntent.UpdateClienteInfo(state.nombreCliente, state.telefono, it)) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = colors.surface, unfocusedContainerColor = colors.surface)
        )
    }
}

@Composable
fun PaymentMethodSection(state: CheckoutState, viewModel: CheckoutViewModel, onUploadClick: () -> Unit, colors: ColorScheme) {
    CheckoutSection(title = "💳 Método de Pago", colors = colors) {
        PaymentMethodOption(
            label = "Tarjeta de Crédito/Débito",
            isSelected = state.metodoPago == "Tarjeta de Crédito",
            onClick = { viewModel.onIntent(CheckoutIntent.UpdatePaymentMethod("Tarjeta de Crédito")) },
            colors = colors
        )
        PaymentMethodOption(
            label = "Pago contra Entrega",
            isSelected = state.metodoPago == "Pago contra Entrega",
            onClick = { viewModel.onIntent(CheckoutIntent.UpdatePaymentMethod("Pago contra Entrega")) },
            colors = colors
        )
        PaymentMethodOption(
            label = "Transferencia Bancaria (Reservar)",
            isSelected = state.metodoPago == "Transferencia Bancaria (Reservar)",
            onClick = { viewModel.onIntent(CheckoutIntent.UpdatePaymentMethod("Transferencia Bancaria (Reservar)")) },
            colors = colors
        )
        if (state.metodoPago == "Transferencia Bancaria (Reservar)") {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onUploadClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (state.comprobanteUri == null) "Subir Comprobante" else "Cambiar Comprobante", color = colors.onPrimary, fontWeight = FontWeight.SemiBold)
            }
            state.comprobanteUri?.let { uri ->
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Comprobante Seleccionado",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun OrderSummarySection(subtotal: String, shipping: String, total: String, colors: ColorScheme) {
    CheckoutSection(title = "📝 Resumen del Pedido", colors = colors) {
        PaymentDetailRow(label = "Subtotal", value = "RD$ $subtotal")
        PaymentDetailRow(label = "Costo de Envío", value = shipping)
        Divider(color = colors.outlineVariant, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
        PaymentDetailRow(label = "Total a Pagar", value = "RD$ $total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

@Composable
fun CheckoutSection(title: String, colors: ColorScheme, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(colors.surface).padding(16.dp)
    ) {
        Text(title, fontSize = 16.sp, color = colors.onSurface, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        content()
    }
}

@Composable
fun PaymentMethodOption(label: String, isSelected: Boolean, onClick: () -> Unit, colors: ColorScheme) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = colors.primary, unselectedColor = colors.onSurfaceVariant))
        Spacer(Modifier.width(8.dp))
        Text(label, fontSize = 16.sp, color = colors.onSurface)
    }
}

@Composable
fun CheckoutBottomBar(total: String, colors: ColorScheme, enabled: Boolean = true, onPlaceOrder: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(colors.surface).padding(horizontal = 16.dp, vertical = 8.dp).height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text("Total", fontSize = 14.sp, color = colors.onSurfaceVariant)
            Text("RD$ $total", fontSize = 22.sp, color = colors.onSurface, fontWeight = FontWeight.ExtraBold)
        }
        Button(
            onClick = onPlaceOrder,
            enabled = enabled,
            modifier = Modifier.width(200.dp).fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Confirmar Pedido", color = colors.onPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun PaymentDetailRow(label: String, value: String, fontWeight: FontWeight = FontWeight.Normal, fontSize: androidx.compose.ui.unit.TextUnit = 16.sp, isLink: Boolean = false) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = fontSize, color = if (isLink) colors.primary else colors.onSurface, fontWeight = fontWeight)
        Text(value, fontSize = fontSize, color = colors.onSurface, fontWeight = fontWeight)
    }
}
