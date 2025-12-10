package com.ebanisterialopez.ebanisterialopez.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ebanisterialopez.ebanisterialopez.presentation.login.LoginViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val state by loginViewModel.state.collectAsState()
    val colors = MaterialTheme.colorScheme

    val userEmail = state.email ?: "No disponible"
    val userName = "Usuario"
    val onLogoutClicked: () -> Unit = {
        loginViewModel.logout()
        navController.navigate(Screens.LOGIN) {
            popUpTo(Screens.HOME) {
                inclusive = true
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = colors.onSurface) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = colors.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = onLogoutClicked) {
                        Icon(
                            Icons.Filled.Logout,
                            contentDescription = "Cerrar Sesión",
                            tint = colors.error
                        )
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .aspectRatio(1f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Avatar de Usuario",
                    modifier = Modifier.size(72.dp),
                    tint = colors.primary // Color primario para el icono
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSurface // Color de texto principal
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Correo dinámico
            Text(
                text = userEmail,
                fontSize = 16.sp,
                color = colors.onSurfaceVariant // Color de texto secundario
            )

            Spacer(modifier = Modifier.height(32.dp))

            Divider(color = colors.outlineVariant, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // Detalles de la cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface), // Fondo de tarjeta
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Detalles de la Cuenta", fontWeight = FontWeight.SemiBold, color = colors.primary) // Título con color primario
                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileDetailRow(label = "Rol", value = "Cliente ", colors = colors)
                    Divider(color = colors.outlineVariant)
                    ProfileDetailRow(label = "Teléfono", value = "809-XXX-XXXX", colors = colors)
                    Divider(color = colors.outlineVariant)
                    ProfileDetailRow(label = "Dirección", value = "No establecida", colors = colors)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onLogoutClicked,
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.error),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(colors.error))
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Cerrar Sesión", modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String, colors: ColorScheme) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontWeight = FontWeight.Normal, color = colors.onSurfaceVariant, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Medium, color = colors.onSurface, fontSize = 14.sp)
    }
}