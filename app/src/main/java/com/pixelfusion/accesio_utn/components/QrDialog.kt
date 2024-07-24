package com.pixelfusion.accesio_utn.components

import android.app.Activity
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog

@Composable
fun FullScreenQRCodeDialog(
    qrCodeBitmap: Bitmap, onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val window = (context as Activity).window
    val originalBrightness = remember { window.attributes.screenBrightness }

    DisposableEffect(Unit) {
        window.attributes = window.attributes.apply {
            screenBrightness = 1.0f
        }
        onDispose {
            window.attributes = window.attributes.apply {
                screenBrightness = originalBrightness
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onDismiss() }, contentAlignment = Alignment.Center
        ) {
            Image(bitmap = qrCodeBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable { onDismiss() })
        }
    }
}