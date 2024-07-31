@file:Suppress("DEPRECATION")

package com.pixelfusion.accesio_utn.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    //primary = Purple80,
    primary = GreenUTN80,
    //secondary = PurpleGrey80,
    secondary = GreenUTNGrey80,
    //tertiary = Pink80
    tertiary = Green80,
    background = DarkTheme,
    //surface = GreenUTN80,
    surface = DarkTheme,
    onPrimary = WhiteColor2,
    onSecondary = WhiteColor2,
    onTertiary = WhiteColor2,
    onBackground = WhiteColor2,
    onSurface = WhiteColor2
)

private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
    primary = GreenUTN40,
    //secondary = PurpleGrey40,
    secondary = GreenUTNGrey40,
    //tertiary = Pink40
    tertiary = Green40,
    background = WhiteColor2,
    //surface = GreenUTN40,
    surface = WhiteColor2,
    onPrimary = black,
    onSecondary = black,
    onTertiary = black,
    onBackground = black,
    onSurface = black

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun AccesIOUTNTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    val context = LocalContext.current
    if (!view.isInEditMode) {
        view.post {
            val window = (context as ComponentActivity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.decorView.systemUiVisibility = if (darkTheme) {
                0 // Clear flags for dark theme
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Light theme: dark icons on light background
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}