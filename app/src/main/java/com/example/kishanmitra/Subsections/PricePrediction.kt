package com.example.kishanmitra.screens.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

private val OrangeGradient = Brush.verticalGradient(
    listOf(Color(0xFFFF7A00), Color(0xFFFF9A3D), Color(0xFFFFC58C))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PricePredictionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Prediction", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(OrangeGradient)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF4E8))
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text("Price Prediction", fontWeight = FontWeight.Bold, color = Color(0xFF4A2A00), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text("Forecast farm‑gate prices", color = Color(0xFF6D3E1E))
            Spacer(Modifier.height(16.dp))
            ElevatedCard {
                Column(Modifier.padding(16.dp)) {
                    Text("Run model and show results here later.", color = Color(0xFF4A2A00))
                }
            }
        }
    }
}
