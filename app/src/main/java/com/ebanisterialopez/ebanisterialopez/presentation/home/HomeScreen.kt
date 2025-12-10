package com.ebanisterialopez.ebanisterialopez.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ebanisterialopez.ebanisterialopez.domain.model.Product
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.math.ceil

data class Category(val name: String, val icon: ImageVector)
data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

private val categories = listOf(
    Category("Bano", Icons.Filled.Bathtub),
    Category("Habitacion", Icons.Filled.Bed),
    Category("Decoraciones", Icons.Filled.AutoFixHigh),
    Category("Metal", Icons.Filled.Hardware),
    Category("Cocina", Icons.Filled.Countertops)
)
private val navItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, "home"),
    BottomNavItem("Contacto", Icons.Filled.Info, "contact"),
    BottomNavItem("Carrito", Icons.Filled.ShoppingCart, "cart"),
    BottomNavItem("Cotizar", Icons.Filled.Description, "quote_screen"),
    BottomNavItem("Setting", Icons.Filled.Settings, "settings")
)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    var searchQuery by remember { mutableStateOf("") }
    val currentRoute = "home"

    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = { TopBarSection(navController, colors) },
        bottomBar = { BottomNavigationBar(navController, colors, currentRoute) }
    ) { paddingValues ->
        when (val state = uiState) {
            is HomeState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            is HomeState.Success -> {
                val products = state.products
                val filteredProducts = if (searchQuery.isBlank()) products
                else products.filter { it.name.contains(searchQuery, ignoreCase = true) }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item { SearchBarSection(value = searchQuery, onValueChange = { searchQuery = it }, colors = colors) }

                    if (searchQuery.isNotBlank()) {
                        items(filteredProducts) { product ->
                            ProductRow(product, navController, colors)
                        }
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    } else {
                        item { CategoriesSection(navController, colors) }
                        item { FeaturedBannerSection(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), colors) }
                        item { WeeklyOffersSection(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), colors) }
                        item {
                            Text(
                                "Productos Destacados",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colors.primary,
                                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                            )
                        }
                        item { ProductGridSection(products, navController, colors) }
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
            is HomeState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}", color = colors.error, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Composable
fun TopBarSection(navController: NavController, colors: ColorScheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Menu, contentDescription = "Menú", modifier = Modifier.size(24.dp), tint = colors.onBackground)
        Spacer(modifier = Modifier.width(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceVariant)
                    .border(1.dp, colors.secondary, CircleShape)
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = colors.secondary, modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.Center))
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Ebanisteria Lopez",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.primary
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant)
                .border(1.dp, colors.secondary, CircleShape)
                .padding(8.dp)
                .clickable { navController.navigate("profile") },
            tint = colors.onBackground
        )
    }
}
@Composable
fun SearchBarSection(value: String, onValueChange: (String) -> Unit, colors: ColorScheme) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Buscar", color = colors.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colors.onSurfaceVariant) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = colors.onSurfaceVariant)
                }
            } else {
                Icon(Icons.Default.Mic, contentDescription = null, tint = colors.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.onSurfaceVariant.copy(alpha = 0.5f),
            cursorColor = colors.primary
        )
    )
}
@Composable
fun CategoriesSection(navController: NavController, colors: ColorScheme) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category -> CategoryItem(category, navController, colors) }
    }
}
@Composable
fun CategoryItem(category: Category, navController: NavController, colors: ColorScheme) {
    val encoded = URLEncoder.encode(category.name, StandardCharsets.UTF_8.toString())
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .width(60.dp)
        .clickable { navController.navigate("category_products/$encoded") }) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(colors.surfaceVariant)
        ) {
            Icon(category.icon, contentDescription = category.name, modifier = Modifier.align(Alignment.Center).size(30.dp), tint = colors.primary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(category.name, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = colors.onBackground)
    }
}

@Composable
fun FeaturedBannerSection(modifier: Modifier = Modifier, colors: ColorScheme) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Nuevos Diseños", color = colors.onPrimary.copy(alpha = 0.8f), fontSize = 14.sp)
                Text("¡Lanzamiento de Colección!", color = colors.onPrimary, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* Navegar a catálogo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.secondary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Ver Catálogo", color = colors.onSecondary, fontSize = 12.sp)
                }
            }
            Icon(
                Icons.Filled.Weekend,
                contentDescription = null,
                tint = colors.onPrimary.copy(alpha = 0.9f),
                modifier = Modifier.size(70.dp)
            )
        }
    }
}
@Composable
fun WeeklyOffersSection(modifier: Modifier = Modifier, colors: ColorScheme) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            "Ofertas Semanales",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colors.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.secondary.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Discount, contentDescription = "Oferta", tint = colors.secondary, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("20% de Descuento", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.secondary)
                    Text("En toda la línea de Baño, solo esta semana.", fontSize = 12.sp, color = colors.onSurface.copy(alpha = 0.7f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Ir", tint = colors.secondary, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun ProductGridSection(products: List<Product>, navController: NavController, colors: ColorScheme) {
    if (products.isEmpty()) return

    val columns = 5
    val rowCount = ceil(products.size.toDouble() / columns).toInt()
    val cardHeight = 160.dp
    val verticalSpacing = 12.dp
    val totalHeight = (rowCount * cardHeight.value + (rowCount - 1).coerceAtLeast(0) * verticalSpacing.value).dp + 16.dp // + 16dp para el padding vertical

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier
            .height(totalHeight)
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        items(products) { product -> ProductCard(product, navController, colors) }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController, colors: ColorScheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("product_detail/${product.productoId}") },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )
            Column(modifier = Modifier.padding(6.dp)) {
                Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, color = colors.onBackground)
                Text(product.description, fontSize = 9.sp, color = colors.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(2.dp))
                Text(product.price, fontWeight = FontWeight.Bold, color = colors.error, fontSize = 10.sp)
            }
        }
    }
}
@Composable
fun ProductRow(product: Product, navController: NavController, colors: ColorScheme) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("product_detail/${product.productoId}") },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.imageUrl.takeIf { it.isNotEmpty() } ?: "https://placehold.co/200x200",
                contentDescription = product.name,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.onBackground)
                Spacer(modifier = Modifier.height(4.dp))
                Text(product.description, fontSize = 14.sp, color = colors.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(product.price, fontWeight = FontWeight.ExtraBold, color = colors.error)
            }
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController, colors: ColorScheme, currentRoute: String) {
    NavigationBar(containerColor = colors.surface, contentColor = colors.primary) {
        navItems.forEach { item ->
            val isSelected = navController.currentDestination?.route == item.route
            val selectedColor = colors.error

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    if (navController.currentDestination?.route != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    selectedTextColor = selectedColor,
                    unselectedIconColor = colors.onSurfaceVariant,
                    unselectedTextColor = colors.onSurfaceVariant
                )
            )
        }
    }
}