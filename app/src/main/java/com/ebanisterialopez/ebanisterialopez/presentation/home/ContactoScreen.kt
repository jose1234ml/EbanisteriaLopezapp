package com.ebanisterialopez.ebanisterialopez.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    nombreNegocio: String = "Ebanisteria Lopez",
    telefono: String = "829-923-6254",
    sitioWeb: String = "https://www.ebanisterialopez.com/",
    instagramUrl: String = "https://www.instagram.com/ebanisterialopez1/",
) {
    val ctx = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val ubicacionUrl = "https://maps.google.com/?q=ebanisterialopez"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos", color = colors.onSurface) }
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = nombreNegocio,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Somos un taller y fábrica dedicada a la ebanistería: puertas, cocinas, mobiliario a medida y proyectos a gran escala. Con más de 48 años de experiencia ofrecemos soluciones desde el diseño hasta la instalación.",
                        fontSize = 14.sp,
                        color = colors.onSurfaceVariant,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Teléfono", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(telefono, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefono"))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ctx.startActivity(intent)
                            } catch (_: Exception) {}
                        }) {
                            Text("Llamar", color = colors.primary)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Página web", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(sitioWeb, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = {
                            try {
                                val i = Intent(Intent.ACTION_VIEW, Uri.parse(sitioWeb))
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ctx.startActivity(i)
                            } catch (_: Exception) {}
                        }) {
                            Text("Abrir", color = colors.primary)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Instagram", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = colors.onSurface)
                            Text(instagramUrl, fontSize = 14.sp, color = colors.onSurfaceVariant)
                        }
                        TextButton(onClick = {
                            try {
                                val i = Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl))
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                ctx.startActivity(i)
                            } catch (_: Exception) {}
                        }) {
                            Text("Abrir", color = colors.primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text("Nuestra Ubicación", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.primary)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ubicacionUrl))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ctx.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.primary.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Ubicación",
                        tint = colors.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ver en el Mapa", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = colors.primary)
                        Text("Haz clic para obtener direcciones.", fontSize = 12.sp, color = colors.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "© ${java.time.Year.now().value} Ebanisteria Lopez",
                fontSize = 12.sp,
                color = colors.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}