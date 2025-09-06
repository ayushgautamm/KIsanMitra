package com.example.kishanmitra.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kishanmitra.ui.theme.KishanMitraTheme
import com.example.kishanmitra.Api.MarketViewModel
import com.example.kishanmitra.Api.UiState
import com.example.kishanmitra.Api.Model.PredictRequest
import com.example.kishanmitra.Api.Model.PredictResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerInputScreen(
    navController: NavHostController,
    vm: MarketViewModel
) {
    var landArea by rememberSaveable { mutableStateOf("") }
    var seedKg by rememberSaveable { mutableStateOf("") }
    var fertilizerKg by rememberSaveable { mutableStateOf("") }
    var pesticideL by rememberSaveable { mutableStateOf("") } // kept for UI parity
    var irrigationHrs by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("") }     // kept for UI parity
    var rainfall by rememberSaveable { mutableStateOf("") }   // kept for UI parity

    fun String.toPositiveDoubleOrNull(): Double? =
        trim().replace(',', '.').toDoubleOrNull()?.takeIf { it >= 0.0 }

    val valid = listOf(landArea, seedKg, fertilizerKg, irrigationHrs, budget)
        .all { it.isNotEmpty() && it.toPositiveDoubleOrNull() != null } &&
            (rainfall.isEmpty() || rainfall.toPositiveDoubleOrNull() != null)

    val headerGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
            MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        )
    )

    val uiState by vm.state.collectAsState()

    // Navigate to marketProjection when API succeeds
    LaunchedEffect(uiState) {
        if (uiState is UiState.Success<PredictResponse>) {
            navController.navigate("marketProjection")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Farmer Input (किसान इनपुट)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                Column(Modifier.fillMaxWidth()) {
                    if (uiState is UiState.Loading) {
                        LinearProgressIndicator(Modifier.fillMaxWidth())
                    }
                    if (uiState is UiState.Error) {
                        Text(
                            text = "Network error: ${(uiState as UiState.Error).message}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            val body = PredictRequest(
                                year = 2024,
                                area = landArea.toDoubleOrNull() ?: 0.0,
                                production = seedKg.toDoubleOrNull() ?: 0.0,
                                yield_per_ha = 5.0,
                                fertilizer = fertilizerKg.toDoubleOrNull() ?: 0.0,
                                irrigation = irrigationHrs.toDoubleOrNull() ?: 0.0,
                                crop = "Rice",
                                season = "kharif",
                            )
                            vm.submit(body)
                        },
                        enabled = valid && uiState !is UiState.Loading,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        )
                    ) {
                        Text("Get Market Projection (अनुमान देखें)", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(headerGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Plan your season inputs",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Content cards
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(
                    title = "Land (भूमि)",
                    highlightColor = MaterialTheme.colorScheme.primary
                ) {
                    NumberField(
                        value = landArea,
                        onChange = { landArea = it },
                        label = "Available Land (उपलब्ध भूमि)",
                        unit = "acres (एकड़)"
                    )
                }

                SectionCard(
                    title = "Inputs (इनपुट)",
                    highlightColor = MaterialTheme.colorScheme.secondary
                ) {
                    NumberField(seedKg, { seedKg = it }, "Seeds/Production (बीज/उत्पादन)", "kg (किग्रा)")
                    NumberField(fertilizerKg, { fertilizerKg = it }, "Fertilizer (खाद)", "kg (किग्रा)")
                    NumberField(pesticideL, { pesticideL = it }, "Pesticide (कीटनाशक)", "L (लीटर)")
                    NumberField(irrigationHrs, { irrigationHrs = it }, "Irrigation (सिंचाई)", "hours (घंटे)")
                    NumberField(rainfall, { rainfall = it }, "Rainfall (वर्षा)", "mm (मिमी)")
                }

                SectionCard(
                    title = "Budget (बजट)",
                    highlightColor = Color(0xFF00BFA6)
                ) {
                    NumberField(
                        value = budget,
                        onChange = { budget = it },
                        label = "Available Budget (उपलब्ध बजट)",
                        unit = "₹ (रु.)",
                        trailingUnitInside = true
                    )
                }

                Spacer(Modifier.height(88.dp))
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    highlightColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
            .padding(1.dp)
    ) {
        // Header strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(highlightColor),
        ) {
            Text(
                text = title.uppercase(),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { content() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NumberField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    unit: String,
    trailingUnitInside: Boolean = false
) {
    var isError by remember(value) { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = { raw ->
            val cleaned = buildString {
                var dot = false
                for (c in raw.replace(',', '.')) {
                    when {
                        c.isDigit() -> append(c)
                        c == '.' && !dot -> { append(c); dot = true }
                        else -> Unit
                    }
                }
            }
            isError = cleaned.isNotEmpty() && cleaned.toDoubleOrNull() == null
            onChange(cleaned)
        },
        label = { Text(label) },
        placeholder = { Text("0") },
        singleLine = true,
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        supportingText = {
            if (isError) Text("Enter a valid number")
            else if (!trailingUnitInside) Text(unit)
        },
        trailingIcon = if (trailingUnitInside) {
            { Text(unit, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFarmerInputScreen() {
    KishanMitraTheme {
        val nav = rememberNavController()
        // Preview requires a fake ViewModel; omitted for brevity.
    }
}
