package com.pixelfusion.accesio_utn.helper

import android.content.Context
import android.graphics.Bitmap
import androidx.print.PrintHelper

fun printQrCode(context: Context, qrCodeBitmap: Bitmap, nombre: String) {
    val printHelper = PrintHelper(context)
    printHelper.scaleMode = PrintHelper.SCALE_MODE_FIT
    printHelper.printBitmap("QR $nombre", qrCodeBitmap)
}