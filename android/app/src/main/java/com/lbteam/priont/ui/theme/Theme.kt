package com.lbteam.priont.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val lightScheme = lightColorScheme(
    primary = DarkGreen90,
    onPrimary = White,
    background = White,
    surface = DarkGreen10,
    onSurface = Black,
    onSurfaceVariant = Grey40,

    surfaceVariant = White,
    outlineVariant = DarkGreenOutline,

    secondaryContainer = White,
    onSecondaryContainer = DarkGreen90
)

@Composable
fun PriontTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicLightColorScheme(context)
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

//        darkTheme -> DarkColorScheme
        else -> lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
//        typography = Typography,
        content = content
    )
}