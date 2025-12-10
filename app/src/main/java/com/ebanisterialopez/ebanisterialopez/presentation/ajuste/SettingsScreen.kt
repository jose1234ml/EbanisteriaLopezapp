package com.ebanisterialopez.ebanisterialopez.presentation.ajuste

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ebanisterialopez.ebanisterialopez.presentation.UserSessionManager
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    darkThemeEnabled: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    sessionManager: UserSessionManager = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val openLanguageDialog = remember { mutableStateOf(false) }
    val openCurrencyDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                "Preferencias de la Aplicación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            SettingSwitchItem(
                icon = Icons.Filled.DarkMode,
                title = "Modo Oscuro",
                description = "Activar o desactivar el tema oscuro.",
                checked = darkThemeEnabled,
                onCheckedChange = { onDarkThemeChange(!darkThemeEnabled) }
            )

            SettingSwitchItem(
                icon = Icons.Filled.Notifications,
                title = "Notificaciones",
                description = "Recibir alertas sobre ofertas y pedidos.",
                checked = state.receiveNotifications,
                onCheckedChange = { viewModel.toggleNotifications(!state.receiveNotifications) }
            )

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )

            SettingClickableItem(
                icon = Icons.Filled.Language,
                title = "Idioma",
                value = state.appLanguage,
                onClick = { openLanguageDialog.value = true }
            )
            SettingClickableItem(
                icon = Icons.Filled.MonetizationOn,
                title = "Moneda",
                value = state.showPriceCurrency,
                onClick = { openCurrencyDialog.value = true }
            )

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline
            )

            SettingClickableItem(
                icon = Icons.Filled.Logout,
                title = "Cerrar Sesión",
                value = "",
                onClick = {
                    sessionManager.clearSession()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                contentColor = MaterialTheme.colorScheme.error
            )
        }
    }

    if (openLanguageDialog.value) {
        LanguageSelectionDialog(
            currentLanguage = state.appLanguage,
            onDismiss = { openLanguageDialog.value = false },
            onSelect = {
                viewModel.setLanguage(it)
                openLanguageDialog.value = false
            }
        )
    }

    if (openCurrencyDialog.value) {
        CurrencySelectionDialog(
            currentCurrency = state.showPriceCurrency,
            onDismiss = { openCurrencyDialog.value = false },
            onSelect = {
                viewModel.setCurrency(it)
                openCurrencyDialog.value = false
            }
        )
    }
}

@Composable
fun SettingClickableItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = contentColor)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = contentColor)
            if (value.isNotEmpty()) {
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
        Icon(
            Icons.Filled.ArrowForwardIos,
            contentDescription = "Ir",
            tint = contentColor.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SettingSwitchItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val languages = listOf("Español", "Inglés", "Francés")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Idioma", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                languages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(lang) }
                            .padding(8.dp)
                    ) {
                        Text(lang, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.primary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}

@Composable
fun CurrencySelectionDialog(
    currentCurrency: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val currencies = listOf("USD", "EUR", "DOP")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar Moneda", color = MaterialTheme.colorScheme.onSurface) },
        text = {
            Column {
                currencies.forEach { currency ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(currency) }
                            .padding(8.dp)
                    ) {
                        Text(currency, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = MaterialTheme.colorScheme.primary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    )
}