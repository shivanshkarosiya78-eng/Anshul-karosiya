package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    secondary = NeonAccent,
    tertiary = Purple80,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = LightSurface,
    onSurfaceVariant = TextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for a premium music experience
    dynamicColor: Boolean = false, // Disable dynamic colors to maintain custom dark theme
    content: @Composable () -> Unit,
) {
    // We always use DarkColorScheme for this premium music streamer to resemble Spotify/JioSaavn
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
