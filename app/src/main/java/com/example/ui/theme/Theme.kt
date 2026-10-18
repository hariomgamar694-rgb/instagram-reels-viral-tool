package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HRViralColorScheme = darkColorScheme(
    primary = NeonPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B1E78),
    onPrimaryContainer = Color(0xFFEDE9FE),
    secondary = NeonCyan,
    onSecondary = Color(0xFF042F2E),
    secondaryContainer = Color(0xFF164E63),
    onSecondaryContainer = Color(0xFFCFFAFE),
    tertiary = NeonPink,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF701A75),
    onTertiaryContainer = Color(0xFFFCE7F3),
    background = DarkNavyBackground,
    onBackground = TextPrimary,
    surface = DarkNavySurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkNavySurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkNavyBorder,
    outlineVariant = Color(0xFF1E2642)
)

@Composable
fun HRViralTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HRViralColorScheme,
        typography = Typography,
        content = content
    )
}
