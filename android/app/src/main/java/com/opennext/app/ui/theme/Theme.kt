package com.opennext.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val OpenNextDarkColorScheme = darkColorScheme(
    primary = Blue40,
    onPrimary = DarkBg,
    primaryContainer = Blue20,
    onPrimaryContainer = Blue80,

    secondary = Blue60,
    onSecondary = DarkBg,
    secondaryContainer = GlowCyanDim,
    onSecondaryContainer = Blue90,

    tertiary = PlayerGreen,
    onTertiary = DarkBg,
    tertiaryContainer = PlayerGreen.copy(alpha = 0.12f),
    onTertiaryContainer = PlayerGreen,

    background = DarkBg,
    onBackground = OnDark,

    surface = DarkSurface,
    onSurface = OnDark,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkVariant,

    error = Error,
    onError = OnDark,
    errorContainer = Error.copy(alpha = 0.12f),
    onErrorContainer = Error,

    outline = OnDarkMuted,
    outlineVariant = OnDarkMuted.copy(alpha = 0.5f),

    inverseSurface = OnDark,
    inverseOnSurface = DarkBg,
    inversePrimary = Blue80,

    surfaceTint = Blue40,
)

@Composable
fun OpenNextTheme(
    content: @Composable () -> Unit,
) {
    val colorScheme = OpenNextDarkColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = OpenNextTypography,
        content = content,
    )
}