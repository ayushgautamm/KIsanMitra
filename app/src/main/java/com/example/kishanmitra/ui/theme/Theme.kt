@file:Suppress("FunctionName")

package com.example.kishanmitra.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OrangePrimary = Color(0xFFFF7A00)      // strong orange
val OrangeSecondary = Color(0xFFFFB74D)    // light orange
val OrangeContainer = Color(0xFFFFE8D6)    // pale orange
val OnOrangeContainer = Color(0xFF4A2A00)  // dark text on pale orange
val AccentTeal = Color(0xFF00BFA6)         // accent for highlights
val SurfaceSoft = Color(0xFFFFFBF7)        // near-white with warmth
val BackgroundSoft = Color(0xFFFFF4E8)     // warm background
val OnSurfaceStrong = Color(0xFF2B2B2B)

private val AppColors: ColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    secondary = OrangeSecondary,
    onSecondary = Color.Black,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OnOrangeContainer,
    background = BackgroundSoft,
    surface = SurfaceSoft,
    onSurface = OnSurfaceStrong,
    outline = Color(0xFFE0C8B0),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun KishanMitraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
