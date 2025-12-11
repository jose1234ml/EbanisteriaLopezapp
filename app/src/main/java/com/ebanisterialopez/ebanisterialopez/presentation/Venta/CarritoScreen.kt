package com.ebanisterialopez.ebanisterialopez.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ebanisterialopez.ebanisterialopez.domain.model.CarritoItem
import com.ebanisterialopez.ebanisterialopez.presentation.Venta.CarritoViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.model.CarritoIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = hiltViewModel(),
    onProceedToPayment: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Carrito de Compras",
                        color = colors.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        bottomBar = {
            if (state.items.isNotEmpty()) {
                BottomBar(
                    orderTotal = String.format("%.2f", state.total),
                    colors = colors
                ) { onProceedToPayment(String.format("%.2f", state.total)) }
            }
        }
    ) { paddingValues ->
        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("El carrito está vacío", color = colors.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colors.background),
                contentPadding = PaddingValues(bottom = 72.dp)
            ) {
                items(state.items) { item ->
                    ShoppingBagItemBlock(item, viewModel, colors)
                    Divider(color = colors.outlineVariant, thickness = 1.dp)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    OrderPaymentDetailsBlock(
                        orderAmount = String.format("%.2f", state.total),
                        orderTotal = String.format("%.2f", state.total),
                        colors = colors
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun BottomBar(orderTotal: String, colors: ColorScheme, onProceedToPayment: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "RD$ $orderTotal",
                fontSize = 20.sp,
                color = colors.error,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Ver detalles",
                fontSize = 14.sp,
                color = colors.onSurfaceVariant
            )
        }

        Button(
            onClick = onProceedToPayment,
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Proceder a Pagar", color = colors.onPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ShoppingBagItemBlock(item: CarritoItem, viewModel: CarritoViewModel, colors: ColorScheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.imageUrl),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surfaceVariant)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontSize = 16.sp, color = colors.onSurface, fontWeight = FontWeight.SemiBold)
            Text("Precio: RD$ ${item.price}", fontSize = 14.sp, color = colors.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { viewModel.onIntent(CarritoIntent.UpdateItem(item.copy(quantity = item.quantity + 1))) },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) { Text("+", color = colors.onPrimary) }

                Spacer(modifier = Modifier.width(8.dp))
                Text("Cantidad: ${item.quantity}", fontSize = 14.sp, color = colors.onSurface)
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (item.quantity > 1) viewModel.onIntent(CarritoIntent.UpdateItem(item.copy(quantity = item.quantity - 1)))
                        else viewModel.onIntent(CarritoIntent.RemoveItem(item))
                    },
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.error),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) { Text("-", color = colors.onError) }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { viewModel.onIntent(CarritoIntent.RemoveItem(item)) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = colors.error)
                }
            }
        }
    }
}

@Composable
fun OrderPaymentDetailsBlock(orderAmount: String, orderTotal: String, colors: ColorScheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "Detalles del Pago",
            fontSize = 16.sp,
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        PaymentDetailRow(label = "Monto de los Artículos", value = "RD$ $orderAmount", colors = colors)
        PaymentDetailRow(label = "Envío", value = "Gratis", isLink = true, colors = colors)
        PaymentDetailRow(
            label = "Total",
            value = "RD$ $orderTotal",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            colors = colors
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun PaymentDetailRow(
    label: String,
    value: String,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    isLink: Boolean = false,
    colors: ColorScheme
) {
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