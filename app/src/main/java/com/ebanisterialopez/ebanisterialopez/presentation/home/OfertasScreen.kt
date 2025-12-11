package com.ebanisterialopez.ebanisterialopez.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(navController: NavController, colors: ColorScheme) {

    val offers = listOf(
        OfferItem(
            title = "20% en línea de Baño - Esta semana",
            details = "Aplica para toda la línea de baño. Válido del 1 al 7 de cada mes. No acumulable con otras promociones."
        ),
        OfferItem(
            title = "15% en Cocinas personalizadas",
            details = "Descuento aplicado al costo de fabricación. Incluye asesoría de diseño. Válido para nuevos pedidos."
        ),
        OfferItem(
            title = "2x1 en accesorios de madera",
            details = "Compra dos accesorios y llévate el segundo gratis. Stock limitado."
        ),
        OfferItem(
            title = "Descuento por remodelación integral",
            details = "Descuentos especiales para proyectos completos de remodelación. Solicita tu cotización."
        )
    )

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ebanistería López",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colors.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(offers) { offer ->
                OfferCardStatic(offer = offer, colors = colors)
            }
        }
    }
}



private data class OfferItem(val title: String, val details: String)




@Composable
private fun OfferCardStatic(offer: OfferItem, colors: ColorScheme) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.secondary.copy(alpha = 0.06f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Filled.LocalOffer,
                contentDescription = null,
                tint = colors.secondary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = offer.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colors.primary
                )

                Spacer(modifier = Modifier.size(6.dp))

                Text(
                    text = offer.details,
                    fontSize = 13.sp,
                    color = colors.onSurface.copy(alpha = 0.8f),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OffersScreenPreview() {
    val colors = MaterialTheme.colorScheme
    OffersScreen(navController = rememberNavController(), colors = colors)
}