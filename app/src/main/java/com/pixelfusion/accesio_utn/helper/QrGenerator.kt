package com.pixelfusion.accesio_utn.helper

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

fun generateQRCodeAssist(text: String, width: Int, height: Int): Bitmap? {
    return try {
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1 // Set margin to 0 to remove border
        }
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
                )
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}


fun generateQRCodePlace(text: String, width: Int, height: Int): Bitmap? {
    return try {
        val hints = hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1 // Set margin to 0 to remove border
        }
        val bitMatrix = QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix[x, y]) Color.Black.toArgb() else Color.White.toArgb()
                )
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}