package com.example.kishanmitra.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kishanmitra.Api.MarketViewModel
import com.example.kishanmitra.Api.UiState
import com.example.kishanmitra.Api.Model.PredictResponse

data class CropProduction(
    val cropName: String,
    val areaAcres: String = "",
    val expectedYieldPerAcreKg: String = "",
) {
    val totalKg: Double
        get() = (areaAcres.toDoubleOrNull() ?: 0.0) *
                (expectedYieldPerAcreKg.toDoubleOrNull() ?: 0.0)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketProjectionScreen(
    navController: NavHostController,
    vm: MarketViewModel
) {
    // Manual list managed on this screen
    val items = remember { mutableStateListOf<CropProduction>() }

    // Collect API state
    val uiState by vm.state.collectAsState()

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF7A00),
            Color(0xFFFF9A3D),
            Color(0xFFFFC58C)
        )
    )

    // Manual calculation
    val totalKg = items.sumOf {
        (it.areaAcres.toDoubleOrNull() ?: 0.0) * (it.expectedYieldPerAcreKg.toDoubleOrNull() ?: 0.0)
    }

    val marketStatus = when {
        totalKg <= 0.0 -> "No Data (कोई डेटा नहीं)"
        totalKg < 5_000.0 -> "Undersupply (कम आपूर्ति)"
        totalKg < 20_000.0 -> "Balanced (संतुलित)"
        else -> "Oversupply (अधिक आपूर्ति)"
    }

    val statusColor = when {
        marketStatus.startsWith("Undersupply") -> Color(0xFF2E7D32)
        marketStatus.startsWith("Balanced") -> Color(0xFFEF6C00)
        marketStatus.startsWith("Oversupply") -> Color(0xFFC62828)
        else -> Color(0xFF6D6D6D)
    }

    val allValid = items.isNotEmpty()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Market Projection (बाज़ार अनुमान)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF7A00)
                )
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color(0xFFFFE8D6)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = marketStatus,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = statusColor
                        )
                    )
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { navController.navigate("cropSuggestion") },
                        enabled = allValid && totalKg > 0.0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF7A00),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFFFB074)
                        )
                    ) {
                        Text("Publish to Sellers")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF4E8))
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner with API + Manual info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(headerGradient),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "📊 Live Market Outlook",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Manual projected output: ${formatKg(totalKg)}",
                        color = Color.White.copy(alpha = 0.95f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    when (uiState) {
                        is UiState.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is UiState.Error -> {
                            Text(
                                text = "❌ API Error: ${(uiState as UiState.Error).message}",
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        is UiState.Success<*> -> {
                            val api = (uiState as UiState.Success<*>).data as? PredictResponse
                            api?.prediction?.let {
                                Text(
                                    text = it,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            api?.probability?.let {
                                Text(
                                    text = "Probability: ${"%.2f".format(it)}",
                                    color = Color.White.copy(alpha = 0.9f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        else -> Unit
                    }
                }
            }

            // Content (manual crops)
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items.forEachIndexed { index, crop ->
                    CropCard(
                        crop = crop,
                        onChange = { updated -> items[index] = updated },
                        onRemove = { items.removeAt(index) }
                    )
                }

                OutlinedButton(
                    onClick = {
                        items.add(
                            CropProduction(
                                cropName = "New Crop (नई फसल)"
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF7A00)
                    )
                ) {
                    Text("＋ Add Crop (फसल जोड़ें)")
                }

                SummaryCard(
                    totalKg = totalKg,
                    status = marketStatus,
                    statusColor = statusColor
                )

                Spacer(Modifier.height(96.dp))
            }
        }
    }
}

@Composable
private fun CropCard(
    crop: CropProduction,
    onChange: (CropProduction) -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFE8D6))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                crop.cropName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A2A00)
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = onRemove) {
                Text("Remove (हटाएँ)", color = Color(0xFFC62828), fontWeight = FontWeight.SemiBold)
            }
        }

        NumberField(
            value = crop.areaAcres,
            onChange = { onChange(crop.copy(areaAcres = it)) },
            label = "Area (क्षेत्र)",
            unit = "acres (एकड़)"
        )

        NumberField(
            value = crop.expectedYieldPerAcreKg,
            onChange = { onChange(crop.copy(expectedYieldPerAcreKg = it)) },
            label = "Expected Yield per Acre (प्रति एकड़ अपेक्षित उपज)",
            unit = "kg/acre (किग्रा/एकड़)"
        )

        val total = (crop.areaAcres.toDoubleOrNull() ?: 0.0) *
                (crop.expectedYieldPerAcreKg.toDoubleOrNull() ?: 0.0)
        Text(
            text = "Projected Total: ${formatKg(total)} (अनुमानित कुल)",
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SummaryCard(
    totalKg: Double,
    status: String,
    statusColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Summary (सारांश)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A2A00)
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Total Output: (कुल उत्पादन) ", fontWeight = FontWeight.SemiBold)
            Text(formatKg(totalKg))
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text("Market Status: (बाज़ार स्थिति) ", fontWeight = FontWeight.SemiBold)
            AssistChip(
                onClick = {},
                label = { Text(status, color = Color.White, fontWeight = FontWeight.Bold) },
                colors = AssistChipDefaults.assistChipColors(containerColor = statusColor)
            )
        }

        Text(
            text = when {
                status.startsWith("Undersupply") ->
                    "Demand is high. Consider increasing output or price. (मांग अधिक है। उत्पादन या कीमत बढ़ाने पर विचार करें।)"
                status.startsWith("Balanced") ->
                    "Market is stable. Maintain planned production. (बाज़ार स्थिर है। नियोजित उत्पादन बनाए रखें।)"
                status.startsWith("Oversupply") ->
                    "Supply is high. Consider lowering price or shifting crop. (आपूर्ति अधिक है। कीमत घटाने या फसल बदलने पर विचार करें।)"
                else ->
                    "Add more data to get a signal. (संकेत प्राप्त करने के लिए अधिक डेटा जोड़ें।)"
            },
            color = Color(0xFF6D6D6D),
            textAlign = TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumberField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    unit: String,
    useLocale: Boolean = false
) {
    val symbols = remember(useLocale) {
        if (useLocale) java.text.DecimalFormatSymbols.getInstance()
        else java.text.DecimalFormatSymbols(java.util.Locale.US)
    }
    val decSep = symbols.decimalSeparator

    fun sanitize(raw: String): String {
        val normalized = raw.replace(',', '.')
        val sb = StringBuilder()
        var dot = false
        for (c in normalized) {
            when {
                c.isDigit() -> sb.append(c)
                c == '.' && !dot -> { sb.append('.'); dot = true }
                else -> Unit
            }
        }
        var out = sb.toString()
        if (out == ".") out = "0."
        return out
    }

    val displayValue = remember(value, useLocale, decSep) {
        if (!useLocale || decSep == '.') value else value.replace('.', decSep)
    }

    OutlinedTextField(
        value = displayValue,
        onValueChange = { rawDisplay ->
            val raw = if (useLocale && decSep == ',') rawDisplay.replace(decSep, '.') else rawDisplay
            val cleaned = sanitize(raw)
            onChange(cleaned)
        },
        label = { Text(label) },
        placeholder = { Text("0") },
        singleLine = true,
        trailingIcon = { Text(unit, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFFF7A00),
            unfocusedBorderColor = Color(0xFFE0C8B0),
            cursorColor = Color(0xFFFF7A00),
            focusedLabelColor = Color(0xFFFF7A00),
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun formatKg(value: Double): String {
    return when {
        value >= 1_000_000 -> String.format("%.2f kt", value / 1_000_000.0)
        value >= 1_000 -> String.format("%.2f t", value / 1_000.0)
        else -> String.format("%.0f kg", value)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMarketProjectionScreen() {
    val navController = rememberNavController()
    // Preview VM would be needed to render API results; omit for now.
}
