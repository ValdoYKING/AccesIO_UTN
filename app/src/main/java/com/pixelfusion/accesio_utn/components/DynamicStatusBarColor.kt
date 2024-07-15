@file:Suppress("DEPRECATION")

package com.pixelfusion.accesio_utn.components

import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import com.pixelfusion.accesio_utn.R

@Composable
fun DynamicStatusBarColor() {
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
    val statusBarColor = if (isDarkTheme) {
        ContextCompat.getColor(context, R.color.utnGreen)
    } else {
        ContextCompat.getColor(context, R.color.utnGreenLight)
    }

    val view = LocalView.current
    view.post {
        val window = (context as ComponentActivity).window
        window.statusBarColor = statusBarColor
        window.decorView.systemUiVisibility = if (isDarkTheme) {
            0 // Clear flags for dark theme
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Light theme: dark icons on light background
        }
    }
}