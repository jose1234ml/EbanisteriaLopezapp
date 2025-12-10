package com.ebanisterialopez.ebanisterialopez.presentation.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val uiState = viewModel.uiState
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
        when (val state = uiState) {
            is CategoryProductsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            is CategoryProductsState.Success -> {
                if (state.products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay productos en la categoría: $categoryName", color = colors.onBackground)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        items(state.products) { product ->
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}", color = colors.error)
                }
            }
        }
    }
}