package se.id21afm.umu.cs.bouldr.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary          = Amber600,
    onPrimary        = Color.White,
    primaryContainer = Amber50,
    onPrimaryContainer = Amber600,

    secondary        = Gray600,
    onSecondary      = Warm50,
    secondaryContainer = Warm100,
    onSecondaryContainer = Gray900,

    background       = Warm50,
    onBackground     = Gray900,

    surface          = Color.White,
    onSurface        = Gray900,
    surfaceVariant   = Warm100,
    onSurfaceVariant = Gray600,

    outline          = Warm200,
    outlineVariant   = Warm200,

    error            = Danger,
    errorContainer   = DangerBg,
)

private val DarkColorScheme = darkColorScheme(
    primary          = Amber100,
    onPrimary        = Gray900,
    primaryContainer = Amber600,
    onPrimaryContainer = Amber50,

    background       = Color(0xFF1C1B18),
    onBackground     = Warm50,

    surface          = Color(0xFF28271F),
    onSurface        = Warm50,
    surfaceVariant   = Color(0xFF33322A),
    onSurfaceVariant = Gray200,

    outline          = Gray600,

    error            = Color(0xFFF09595),
    errorContainer   = Danger,
)

@Composable
fun BouldRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography  = Typography,
        content     = content
    )
}