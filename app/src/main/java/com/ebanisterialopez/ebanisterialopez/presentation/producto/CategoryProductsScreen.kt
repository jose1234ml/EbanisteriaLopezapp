package com.ebanisterialopez.ebanisterialopez.presentation.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ebanisterialopez.ebanisterialopez.presentation.home.ProductCard
import com.ebanisterialopez.ebanisterialopez.presentation.model.CategoryProductsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    navController: NavController,
    viewModel: CategoryProductsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState() // Observamos StateFlow
    val categoryName = viewModel.categoryName
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(categoryName, color = colors.onBackground) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = colors.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.onBackground,
                    navigationIconContentColor = colors.primary
                )
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is CategoryProductsState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }
                is CategoryProductsState.Success -> {
                    val products = (state as CategoryProductsState.Success).products
                    if (products.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay productos en la categoría: $categoryName", color = colors.onBackground)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(products) { product ->
                                ProductCard(
                                    product = product,
                                    navController = navController,
                                    colors = colors
                                )
                            }
                        }
                    }
                }
                is CategoryProductsState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${(state as CategoryProductsState.Error).message}", color = colors.error)
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewCategoryProductsScreen() {
    val colors = MaterialTheme.colorScheme
    val mockProducts = listOf(
        com.ebanisterialopez.ebanisterialopez.domain.model.Product(
            productoId = 1,
            name = "Mesa de Roble",
            description = "Mesa robusta de roble",
            price = "RD$ 12,000",
            imageUrl = "https://placehold.co/600x400/cccccc/000000?text=Mesa",
            color = "Natural",
            material = "Roble",
            dimensiones = "200x100x75 cm",
            images = emptyList()
        ),
        com.ebanisterialopez.ebanisterialopez.domain.model.Product(
            productoId = 2,
            name = "Silla Clásica",
            description = "Silla cómoda de madera",
            price = "RD$ 3,500",
            imageUrl = "https://placehold.co/600x400/aaaaaa/000000?text=Silla",
            color = "Madera natural",
            material = "Roble",
            dimensiones = "50x50x90 cm",
            images = emptyList()
        )
    )
    val fakeState = remember {
        androidx.compose.runtime.mutableStateOf<CategoryProductsState>(
            CategoryProductsState.Success(mockProducts)
        )
    }
    @Composable
    fun TempCategoryProductsScreen(state: CategoryProductsState) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is CategoryProductsState.Loading -> CircularProgressIndicator(color = colors.primary)
                is CategoryProductsState.Success -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.products) { product ->
                        com.ebanisterialopez.ebanisterialopez.presentation.home.ProductCard(
                            product = product,
                            navController = androidx.navigation.compose.rememberNavController(),
                            colors = colors
                        )
                    }
                }
                is CategoryProductsState.Error -> Text("Error: ${state.message}", color = colors.error)
            }
        }
    }

    TempCategoryProductsScreen(state = fakeState.value)
}