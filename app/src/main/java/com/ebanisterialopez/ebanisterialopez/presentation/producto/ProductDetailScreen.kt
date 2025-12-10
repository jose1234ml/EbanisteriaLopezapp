package com.ebanisterialopez.ebanisterialopez.presentation.producto

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import com.ebanisterialopez.ebanisterialopez.presentation.Venta.CarritoViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.product_detail.ProductDetailState
import com.ebanisterialopez.ebanisterialopez.presentation.product_detail.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel(),
    carritoViewModel: CarritoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto", color = colors.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = colors.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Toggle Favorite */ }) {
                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorito", tint = colors.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.primary)
            )
        },
        bottomBar = {
            if (uiState is ProductDetailState.Success) {
                BottomDetailBar(
                    product = (uiState as ProductDetailState.Success).product,
                    navController = navController,
                    carritoViewModel = carritoViewModel,
                    colors = colors
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colors.background)
        ) {
            when (val state = uiState) {
                is ProductDetailState.Loading -> CircularProgressIndicator(
                    color = colors.primary,
                    modifier = Modifier.align(Alignment.Center)
                )
                is ProductDetailState.Success -> ProductDetailContent(state.product, colors)
                is ProductDetailState.Error -> Text(
                    "Error: ${state.message}",
                    color = colors.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun ProductDetailContent(product: Product, colors: ColorScheme) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        item {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .background(colors.surfaceVariant)
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(colors.surface)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                Spacer(Modifier.height(6.dp))
                Text("ID: ${product.productoId}", fontSize = 13.sp, color = colors.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Divider(color = colors.outlineVariant)
                Spacer(Modifier.height(12.dp))
                Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = colors.onSurface)
                Spacer(Modifier.height(6.dp))
                Text(product.description, style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
                Spacer(Modifier.height(12.dp))
                Divider(color = colors.outlineVariant)
                Spacer(Modifier.height(12.dp))
                Text("Características", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = colors.onSurface)
                Spacer(Modifier.height(8.dp))
                if (!product.material.isNullOrBlank()) DetailRow("Material", product.material!!, colors)
                if (!product.color.isNullOrBlank()) DetailRow("Color", product.color!!, colors)
                if (!product.dimensiones.isNullOrBlank()) DetailRow("Dimensiones", product.dimensiones!!, colors)
                if (product.material.isNullOrBlank() && product.color.isNullOrBlank() && product.dimensiones.isNullOrBlank())
                    Text("No hay características disponibles.", color = colors.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Divider(color = colors.outlineVariant)
                Spacer(Modifier.height(12.dp))
                val imgs = product.images
                if (imgs.isNotEmpty()) {
                    Text("Imágenes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = colors.onSurface)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        items(imgs) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(220.dp, 140.dp)
                                    .background(colors.surfaceVariant, shape = RoundedCornerShape(8.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun DetailRow(label: String, value: String, colors: ColorScheme) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = colors.primary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, fontWeight = FontWeight.SemiBold, color = colors.onSurface)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = colors.onSurface)
        }
    }
}
@Composable
fun BottomDetailBar(
    product: Product,
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    colors: ColorScheme
) {
    Surface(
        Modifier.fillMaxWidth().height(80.dp),
        color = colors.surface,
        shadowElevation = 10.dp
    ) {
        Row(
            Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Precio", fontSize = 14.sp, color = colors.onSurfaceVariant)
                Text(
                    product.price,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.error
                )
            }

            Button(
                onClick = {
                    carritoViewModel.addItem(product, 1)
                    navController.navigate("cart")
                },
                Modifier.fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                shape = RoundedCornerShape(50)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp), tint = colors.onPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir al Carrito", fontWeight = FontWeight.SemiBold, color = colors.onPrimary)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductDetailScreen() {
    val mock = Product(
        productoId = 101,
        name = "Mesa de Roble Clásica",
        description = "Mesa robusta de roble, ideal para comedor. Capacidad 8 personas.",
        price = "RD$ 12,000",
        imageUrl = "https://placehold.co/800x400/0D47A1/FFFFFF?text=Mesa",
        material = "Madera: Roble",
        color = "Natural",
        dimensiones = "200 x 100 x 75 cm",
        images = listOf(
            "https://placehold.co/600x400/cccccc/000000?text=Imagen1",
            "https://placehold.co/600x400/aaaaaa/000000?text=Imagen2"
        )
    )
    MaterialTheme {
        ProductDetailContent(mock, MaterialTheme.colorScheme)
    }
}