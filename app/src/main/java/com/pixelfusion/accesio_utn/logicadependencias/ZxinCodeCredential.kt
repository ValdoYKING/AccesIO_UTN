package com.pixelfusion.accesio_utn.logicadependencias

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


fun generateBarcodeCode39(content: String, width: Int = 700, height: Int = 100): Bitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.CODE_39,
            width,
            height
        )
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun generateBarcodeCode128(content: String, width: Int, height: Int): Bitmap? {
    return try {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.CODE_128,
            width,
            height
        )
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun generateQRCode(
    hora: String,
    fecha: String,
    matricula: String,
    UID: String,
    width: Int = 300,
    height: Int = 300
): Bitmap? {
    return try {
        //val qrContent = "Hora: $hora\nFecha: $fecha\nMatrícula: $matricula \nUID: $UID"

        val qrContentArray = listOf(
            "Hora: $hora",
            "Fecha: $fecha",
            "Matrícula:$matricula",
            "UID: $UID"
        )

        val qrContent = "$hora,$fecha,$matricula,$UID"
        //val qrContent = hora fecha matricula UID

        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            //qrContent,
            qrContent,
            BarcodeFormat.QR_CODE,
            width,
            height
        )
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}