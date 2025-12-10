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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ebanisterialopez.ebanisterialopez.presentation.Venta.CheckoutViewModel
import com.ebanisterialopez.ebanisterialopez.domain.model.Venta
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    orderTotal: String,
    onBack: () -> Unit,
    onPlaceOrder: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val colors = MaterialTheme.colorScheme
    val subtotalValue = orderTotal
    val shippingCost = "Gratis"
    val totalWithShipping = orderTotal

    var selectedPaymentMethod by remember { mutableStateOf("Tarjeta de Crédito") }
    var nombreCliente by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var comprobanteUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        comprobanteUri = uri
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val isLoading by remember { derivedStateOf { viewModel.isLoading } }
    val errorMessage by remember { derivedStateOf { viewModel.errorMessage } }
    LaunchedEffect(errorMessage) {
        errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearErrorMessage()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Proceder a Pagar",
                        color = colors.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = colors.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            CheckoutBottomBar(
                total = totalWithShipping,
                colors = colors,
                onPlaceOrder = {
                    if (nombreCliente.isBlank() || telefono.isBlank() || direccion.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor completa los datos de dirección.")
                        }
                        return@CheckoutBottomBar
                    }
                    if (selectedPaymentMethod == "Transferencia Bancaria (Reservar)" && comprobanteUri == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor sube el comprobante de la transferencia.")
                        }
                        return@CheckoutBottomBar
                    }

                    val venta = Venta(
                        nombreCliente = nombreCliente,
                        telefono = telefono,
                        direccion = direccion,
                        metodoPago = selectedPaymentMethod,
                        comprobanteUri = comprobanteUri
                    )
                    viewModel.confirmarPedido(venta)
                    onPlaceOrder()
                },
                enabled = !isLoading
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CheckoutSection(title = "📦 Dirección de Envío", colors = colors) {

                    OutlinedTextField(
                        value = nombreCliente,
                        onValueChange = { nombreCliente = it },
                        label = { Text("Nombre del Cliente") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = colors.surface,
                            unfocusedContainerColor = colors.surface,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                CheckoutSection(title = "💳 Método de Pago", colors = colors) {
                    PaymentMethodOption(
                        label = "Tarjeta de Crédito/Débito",
                        isSelected = selectedPaymentMethod == "Tarjeta de Crédito",
                        onClick = { selectedPaymentMethod = "Tarjeta de Crédito" },
                        colors = colors
                    )
                    PaymentMethodOption(
                        label = "Pago contra Entrega",
                        isSelected = selectedPaymentMethod == "Pago contra Entrega",
                        onClick = { selectedPaymentMethod = "Pago contra Entrega" },
                        colors = colors
                    )
                    PaymentMethodOption(
                        label = "Transferencia Bancaria (Reservar)",
                        isSelected = selectedPaymentMethod == "Transferencia Bancaria (Reservar)",
                        onClick = { selectedPaymentMethod = "Transferencia Bancaria (Reservar)" },
                        colors = colors
                    )
                    if (selectedPaymentMethod == "Transferencia Bancaria (Reservar)") {
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (comprobanteUri == null) "Subir Comprobante" else "Cambiar Comprobante",
                                color = colors.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        comprobanteUri?.let { uri ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Comprobante Seleccionado",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CheckoutSection(title = "📝 Resumen del Pedido", colors = colors) {
                    PaymentDetailRow(label = "Subtotal", value = "RD$ $subtotalValue")
                    PaymentDetailRow(label = "Costo de Envío", value = shippingCost)
                    Divider(color = colors.outlineVariant, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    PaymentDetailRow(
                        label = "Total a Pagar",
                        value = "RD$ $totalWithShipping",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.scrim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        }
    }
}
@Composable
fun CheckoutSection(title: String, colors: ColorScheme, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surface)
            .padding(16.dp)
    ) {
        Text(
            title,
            fontSize = 16.sp,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
}
@Composable
fun PaymentMethodOption(label: String, isSelected: Boolean, onClick: () -> Unit, colors: ColorScheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = colors.primary, unselectedColor = colors.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 16.sp, color = colors.onSurface)
    }
}
@Composable
fun CheckoutBottomBar(total: String, colors: ColorScheme, onPlaceOrder: () -> Unit, enabled: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
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
fun PaymentDetailRow(
    label: String,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    isLink: Boolean = false
) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = fontSize, color = colors.onSurface, fontWeight = fontWeight)
        Text(
            value,
            fontSize = fontSize,
            color = if (isLink) colors.primary else colors.onSurface,
            fontWeight = fontWeight
        )
    }
}