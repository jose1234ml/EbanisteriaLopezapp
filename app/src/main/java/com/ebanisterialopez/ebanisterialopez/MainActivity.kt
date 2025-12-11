package com.ebanisterialopez.ebanisterialopez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ebanisterialopez.ebanisterialopez.presentation.*
import com.ebanisterialopez.ebanisterialopez.presentation.Venta.CarritoViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.cotizacion.CotizacionScreen
import com.ebanisterialopez.ebanisterialopez.presentation.producto.CategoryProductsScreen
import com.ebanisterialopez.ebanisterialopez.presentation.home.HomeScreen
import com.ebanisterialopez.ebanisterialopez.presentation.home.OffersScreen
import com.ebanisterialopez.ebanisterialopez.presentation.ajuste.SettingsScreen
import com.ebanisterialopez.ebanisterialopez.presentation.login.LoginScreen
import com.ebanisterialopez.ebanisterialopez.presentation.login.RegisterScreen
import com.ebanisterialopez.ebanisterialopez.presentation.product_detail.ProductDetailScreen
import dagger.hilt.android.AndroidEntryPoint
import com.example.compose.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var darkThemeEnabled by rememberSaveable { mutableStateOf(systemDarkTheme) }

            AppTheme(darkTheme = darkThemeEnabled) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        darkThemeEnabled = darkThemeEnabled,
                        onDarkThemeChange = { darkThemeEnabled = it }
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    darkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN
    ) {
        composable(Screens.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screens.HOME) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Screens.REGISTER) }
            )
        }
        composable(Screens.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screens.HOME) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                },
                onLoginClick = { navController.navigate(Screens.LOGIN) }
            )
        }
        composable(Screens.HOME) {
            HomeScreen(navController = navController)
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                darkThemeEnabled = darkThemeEnabled,
                onDarkThemeChange = onDarkThemeChange
            )
        }

        composable(Screens.QUOTE) {
            CotizacionScreen(navController = navController)
        }

        composable(Screens.CONTACT) {
            ContactScreen()
        }

        composable(
            route = "category_products/{categoryName}",
            arguments = listOf(navArgument("categoryName") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            CategoryProductsScreen(navController = navController)
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            ProductDetailScreen(navController = navController)
        }

        composable("offers") {
            OffersScreen(navController = navController, colors = MaterialTheme.colorScheme)
        }

        composable(Screens.CART) {
            val carritoViewModel: CarritoViewModel = hiltViewModel()
            CarritoScreen(
                viewModel = carritoViewModel,
                onProceedToPayment = { orderTotal ->
                    navController.navigate("${Screens.CHECKOUT}/$orderTotal")
                }
            )
        }
        composable(
            route = "${Screens.CHECKOUT}/{orderTotal}",
            arguments = listOf(navArgument("orderTotal") { type = NavType.StringType })
        ) { backStackEntry ->
            val total = backStackEntry.arguments?.getString("orderTotal") ?: "0.00"
            CheckoutScreen(
                orderTotal = total,
                onBack = { navController.popBackStack() },
                onPlaceOrder = { }
            )
        }
    }
}