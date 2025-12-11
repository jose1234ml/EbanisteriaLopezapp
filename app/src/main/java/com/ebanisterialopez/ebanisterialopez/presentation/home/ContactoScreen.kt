package com.ebanisterialopez.ebanisterialopez.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ebanisterialopez.ebanisterialopez.presentation.home.ContactViewModel
import com.ebanisterialopez.ebanisterialopez.presentation.model.ContactIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    contactViewModel: ContactViewModel = viewModel()
) {
    val uiState = contactViewModel.uiState.collectAsState().value
    val colors = MaterialTheme.colorScheme

    Scaffold(
        topBar = { TopAppBar(title = { Text("Contactos", color = colors.onSurface) }) },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Card de Información del Negocio
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(uiState.nombreNegocio, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = colors.primary)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Somos un taller y fábrica dedicada a la ebanistería: puertas, cocinas, mobiliario a medida y proyectos a gran escala. Con más de 48 años de experiencia ofrecemos soluciones desde el diseño hasta la instalación.",
                        fontSize = 14.sp, color = colors.onSurfaceVariant, textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Card de Contactos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Teléfono", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(uiState.telefono, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = { contactViewModel.onIntent(ContactIntent.Llamar) }) {
                            Text("Llamar", color = colors.primary)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Página web", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(uiState.sitioWeb, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = { contactViewModel.onIntent(ContactIntent.AbrirWeb) }) {
                            Text("Abrir", color = colors.primary)
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Instagram", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(uiState.instagramUrl, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = { contactViewModel.onIntent(ContactIntent.AbrirInstagram) }) {
                            Text("Abrir", color = colors.primary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Text("Nuestra Ubicación", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.primary)
            Spacer(Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable { contactViewModel.onIntent(ContactIntent.VerUbicacion) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primary.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Ubicación", tint = colors.primary, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ver en el Mapa", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.primary)
                        Text("Haz clic para obtener direcciones.", fontSize = 12.sp, color = colors.onSurfaceVariant)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text("© ${java.time.Year.now().value} ${uiState.nombreNegocio}",
                fontSize = 12.sp,
                color = colors.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactScreenPreview() {
    ContactScreen()
}