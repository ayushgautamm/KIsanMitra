package com.example.kishanmitra.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kishanmitra.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictedPriceScreen( // rename to CropDetailScreen if you prefer
    cropName: String,
    imageRes: Int? = null,
    onBack: (() -> Unit)? = null
) {
    val predictedPricePerQuintal = remember(cropName) { mockPredictPricePerQuintal(cropName) }

    // If no image is provided, map by crop name (supports bilingual labels)
    val effectiveImageRes = imageRes ?: cropImageForName(cropName)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$cropName — Predicted Price (अनुमानित कीमत)") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Image card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = effectiveImageRes),
                    contentDescription = cropName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // Title
            Text(
                text = cropName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )

            // Price card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Estimated farm-gate price (अनुमानित खेत-स्तरीय कीमत)",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "₹$predictedPricePerQuintal per quintal (प्रति क्विंटल)",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Note: This is a projected estimate for demonstration. Integrate your market feed or ML model for live predictions. (टिप्पणी: यह केवल प्रदर्शन हेतु अनुमान है। लाइव भविष्यवाणी के लिए अपने मार्केट फ़ीड या ML मॉडल को जोड़ें।)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Actions
            Button(
                onClick = { /* TODO: Navigate to insights */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Market Insights (मार्केट इनसाइट्स देखें)")
            }
        }
    }
}

// Map a crop name (supports bilingual versions) to a drawable
private fun cropImageForName(name: String): Int {
    val key = name.trim().lowercase()
    return when {
        key.startsWith("wheat") || key.contains("गेहूं") -> R.drawable.wheat
        key.startsWith("rice") || key.contains("चावल") -> R.drawable.rice
        key.startsWith("maize") || key.startsWith("corn") || key.contains("मक्का") -> R.drawable.maize
        key.startsWith("potato") || key.contains("आलू") -> R.drawable.patato
        key.startsWith("tomato") || key.contains("टमाटर") -> R.drawable.tomato
        key.startsWith("sugarcane") || key.contains("गन्ना") -> R.drawable.sugarcane
        key.startsWith("sunflower") || key.contains("सूरजमुखी") -> R.drawable.sunflower
        key.startsWith("groundnut") || key.startsWith("peanut") || key.contains("मूंगफली") -> R.drawable.groundnut
        key.startsWith("banana") || key.contains("केला") -> R.drawable.banana
        key.startsWith("mango") || key.contains("आम") -> R.drawable.mango
        else -> R.drawable.crop_placeholder // add a generic placeholder image
    }
}

// Mock predictor for demo; replace with real data/model later
private fun mockPredictPricePerQuintal(cropName: String): Int {
    val base = when (cropName.lowercase()) {
        "wheat", "wheat (गेहूं)" -> 2350
        "rice", "rice (चावल)" -> 2400
        "maize", "maize (मक्का)", "corn" -> 2100
        "potato" -> 1200
        "tomato" -> 1600
        "sugarcane" -> 3200
        "sunflower" -> 4200
        "groundnut", "peanut" -> 5200
        "banana" -> 1800
        "mango" -> 6000
        else -> 2000
    }
    val variance = ((cropName.hashCode() % 7 + 7) % 7) * 25 // 0..150
    return base + variance
}

@Preview(showBackground = true)
@Composable
private fun PredictedPriceScreenPreview_Wheat() {
    PredictedPriceScreen(
        cropName = "Wheat (गेहूं)",
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PredictedPriceScreenPreview_Rice() {
    PredictedPriceScreen(
        cropName = "Rice (चावल)",
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PredictedPriceScreenPreview_Maize() {
    PredictedPriceScreen(
        cropName = "Maize (मक्का)",
        onBack = {}
    )
}
