package com.pixelfusion.accesio_utn.helper

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Environment
import okio.IOException
import java.io.File
import java.io.FileOutputStream

fun createPdfFromQrCode(qrCodeBitmap: Bitmap, fileName: String) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(qrCodeBitmap.width, qrCodeBitmap.height, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    canvas.drawBitmap(qrCodeBitmap, 0f, 0f, null)
    pdfDocument.finishPage(page)

    val filePath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "$fileName.pdf"
    )
    try {
        pdfDocument.writeTo(FileOutputStream(filePath))
    } catch (e: IOException) {
        e.printStackTrace()
    }
    pdfDocument.close()
}