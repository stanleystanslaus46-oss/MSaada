package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MsaadaTeal,
    onPrimary = MsaadaWhite,
    primaryContainer = MsaadaDarkCard,
    onPrimaryContainer = MsaadaTealLight,
    secondary = MsaadaTeal,
    onSecondary = MsaadaWhite,
    secondaryContainer = MsaadaDarkSurface,
    onSecondaryContainer = MsaadaTealLight,
    tertiary = MsaadaProGold,
    onTertiary = MsaadaWhite,
    background = MsaadaDarkNavy,
    onBackground = MsaadaDarkText,
    surface = MsaadaDarkSurface,
    onSurface = MsaadaDarkText,
    surfaceVariant = MsaadaDarkCard,
    onSurfaceVariant = MsaadaDarkTextSecondary,
    outline = MsaadaDarkBorder,
    outlineVariant = MsaadaDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = MsaadaNavy,
    onPrimary = MsaadaWhite,
    primaryContainer = MsaadaTealLight,
    onPrimaryContainer = MsaadaNavy,
    secondary = MsaadaTeal,
    onSecondary = MsaadaWhite,
    secondaryContainer = MsaadaTealLight,
    onSecondaryContainer = MsaadaTealDark,
    tertiary = MsaadaProGold,
    onTertiary = MsaadaWhite,
    background = MsaadaBackground,
    onBackground = MsaadaCharcoal,
    surface = MsaadaWhite,
    onSurface = MsaadaCharcoal,
    surfaceVariant = MsaadaGrayLight,
    onSurfaceVariant = MsaadaGrayDark,
    outline = Color(0xFFE2E8F0),
    outlineVariant = Color(0xFFEDF2F7)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
