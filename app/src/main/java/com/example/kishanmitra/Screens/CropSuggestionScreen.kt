package com.example.kishanmitra.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kishanmitra.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset


data class Crop(val name: String, val imageRes: Int)

private val sampleCrops = listOf(
    Crop("Wheat (गेहूं)", R.drawable.wheat),
    Crop("Maize (मक्का)", R.drawable.maize),
    Crop("Rice (चावल)", R.drawable.rice),
    Crop("Potato (आलू)", R.drawable.patato),
    Crop("Tomato (टमाटर)", R.drawable.tomato),
    Crop("Sugarcane (गन्ना)", R.drawable.sugarcane),
    Crop("Sunflower (सूरजमुखी)", R.drawable.sunflower),
    Crop("Groundnut (मूंगफली)", R.drawable.groundnut),
    Crop("Banana (केला)", R.drawable.banana),
    Crop("Mango (आम)", R.drawable.mango)
)

// Reusable contract for overflow menu items
interface TopMenuItem {
    val title: String
    fun onClick(nav: NavHostController)
}

// Sample menu items; update destinations later when new screens are added
private class SettingsMenuItem : TopMenuItem {
    override val title: String = "Settings"
    override fun onClick(nav: NavHostController) {
        // TODO: replace with nav.navigate("settings") when route exists
        nav.navigate("farmerInput")
    }
}
private class AboutMenuItem : TopMenuItem {
    override val title: String = "About"
    override fun onClick(nav: NavHostController) {
        // TODO: replace with nav.navigate("about") when route exists
        nav.navigate("cropSuggestion")
    }
}

private val topMenuItems: List<TopMenuItem> = listOf(
    SettingsMenuItem(),
    AboutMenuItem()
)

@Composable
private fun CropList(
    crops: List<Crop>,
    onCropClick: (Crop) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 88.dp)
    ) {
        items(
            items = crops,
            key = { it.name }
        ) { crop ->
            CropItem(
                crop = crop,
                onClick = { onCropClick(crop) }
            )
        }
    }
}

@Composable
private fun CropItem(
    crop: Crop,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = crop.imageRes),
                contentDescription = crop.name,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = crop.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tap to see predicted yield & payout\nअनुमानित उपज और भुगतान देखने के लिए टैप करें",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropSuggestionScreen(navController: NavHostController) {
    var menuExpanded by remember { mutableStateOf(false) }

    // Vibrant orange gradient (top → bottom)
    val orangeGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF7A00), // deep orange
            Color(0xFFFF9A3D), // mid orange
            Color(0xFFFFC58C)  // soft peach
        )
    ) // rich, warm header [3]

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(orangeGradient) // paint gradient under bar [2]
                    .shadow(6.dp),               // soft depth for the bar [7]
                title = {
                    Text(
                        "Crop Suggestions",
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        offset = DpOffset(x = 0.dp, y = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        containerColor = Color(0xFFFFF1E3),
                        tonalElevation = 8.dp,
                        shadowElevation = 10.dp
                    ) {
                        // SETTINGS (already present)
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Settings, contentDescription = null, tint = Color(0xFFB34700))
                            },
                            text = {
                                Column(Modifier.fillMaxWidth()) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Settings",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = Color(0xFF4A2A00)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Surface(color = Color(0xFFFF7A00), shape = RoundedCornerShape(8.dp)) {
                                            Text("New", color = Color.White, style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    Spacer(Modifier.height(2.dp))
                                    Text("Theme, language, notifications",
                                        style = MaterialTheme.typography.bodySmall, color = Color(0xFF6D3E1E))
                                }
                            },
                            onClick = { menuExpanded = false; navController.navigate("farmerInput") },
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFF4A2A00),
                                leadingIconColor = Color(0xFF4A2A00),
                                trailingIconColor = Color(0xFF4A2A00)
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                        )

                        // ABOUT (already present)
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(Icons.Filled.Info, contentDescription = null, tint = Color(0xFFB34700))
                            },
                            text = {
                                Column(Modifier.fillMaxWidth()) {
                                    Text("About",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF4A2A00)
                                    )
                                    Spacer(Modifier.height(2.dp))
                                    Text("Version, credits, contact",
                                        style = MaterialTheme.typography.bodySmall, color = Color(0xFF6D3E1E))
                                }
                            },
                            onClick = { menuExpanded = false; navController.navigate("cropSuggestion") },
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFF4A2A00),
                                leadingIconColor = Color(0xFF4A2A00),
                                trailingIconColor = Color(0xFF4A2A00)
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                        )

                        // Divider for emphasis grouping
                        HorizontalDivider(color = Color(0xFFFFD3AD))

                        // Helper to render fancy orange item with title + subtitle
                        @Composable
                        fun orangeMenuItem(
                            title: String,
                            subtitle: String,
                            onClick: () -> Unit
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Column(Modifier.fillMaxWidth()) {
                                        Text(
                                            title,
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = Color(0xFF4A2A00)
                                        )
                                        Spacer(Modifier.height(2.dp))
                                        Text(
                                            subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF6D3E1E)
                                        )
                                    }
                                },
                                onClick = { menuExpanded = false; onClick() },
                                colors = MenuDefaults.itemColors(
                                    textColor = Color(0xFF4A2A00),
                                    leadingIconColor = Color(0xFF4A2A00),
                                    trailingIconColor = Color(0xFF4A2A00)
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                            )
                        }

                        // Add the commented items below with catchy subtitles
                        // Real Time Interaction
                        orangeMenuItem("Real Time Interaction", "Farmer Inputs and instant coordination") {
                            menuExpanded = false
                            navController.navigate("rtInteraction")
                        }
// Optimal Crop
                        orangeMenuItem("Optimal Crop", "Smart suggestions based on inputs") {
                            menuExpanded = false
                            navController.navigate("optimalCrop")
                        }
// Price Prediction
                        orangeMenuItem("Price Prediction", "Forecast farm‑gate prices") {
                            menuExpanded = false
                            navController.navigate("pricePrediction")
                        }
// Cultivation Input
                        orangeMenuItem("Cultivation Input", "Seeds, fertilizer, irrigation planning") {
                            menuExpanded = false
                            navController.navigate("cultivationInput")
                        }
// Kisan Mitra Seva
                        orangeMenuItem("Kisan Mitra Seva", "Services and assistance hub") {
                            menuExpanded = false
                            navController.navigate("kisanMitraSeva")
                        }
// Complaints Section
                        orangeMenuItem("Complaints Section", "Raise issues and track status") {
                            menuExpanded = false
                            navController.navigate("complaints")
                        }
// Calamity Alarm
                        orangeMenuItem("Calamity Alarm", "Weather alerts and risk signals") {
                            menuExpanded = false
                            navController.navigate("calamityAlarm")
                        }
// Market Rating: Color Zones
                        orangeMenuItem("Market Rating: Color Zones", "Green, amber, red market heat") {
                            menuExpanded = false
                            navController.navigate("marketRating")
                        }
// Crop Info
                        orangeMenuItem("Crop Info", "Best practices and guides") {
                            menuExpanded = false
                            navController.navigate("cropInfo")
                        }
// AI Helper
                        orangeMenuItem("AI Helper", "Ask anything—farming assistant") {
                            menuExpanded = false
                            navController.navigate("aiHelper")
                        }
// Consumer Helpline
                        orangeMenuItem("Consumer Helpline", "Support and escalation") {
                            menuExpanded = false
                            navController.navigate("consumerHelpline")
                        }
// Industrial Agreement
                        orangeMenuItem("Industrial Agreement", "Contract options and terms") {
                            menuExpanded = false
                            navController.navigate("industrialAgreement")
                        }
// Direct Consumer Reach
                        orangeMenuItem("Direct Consumer Reach", "Sell directly—build visibility") {
                            menuExpanded = false
                            navController.navigate("directReach")
                        }

                    }



                },
                // make the app bar itself transparent so gradient shows through
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // unchanged
            BottomAppBar(tonalElevation = 2.dp) {
                Button(
                    onClick = { navController.navigate("farmerInput") },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Next → Enter My Stock")
                }
            }
        }
    ) { padding ->
        // existing content unchanged
        CropList(
            crops = sampleCrops,
            onCropClick = { crop ->
                val encoded = URLEncoder.encode(crop.name, StandardCharsets.UTF_8.toString())
                navController.navigate("cropDetail/$encoded")
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCropSuggestionScreen() {
    val navController = rememberNavController()
    CropSuggestionScreen(navController)
}
