package com.pixelfusion.accesio_utn.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun FechaATexto(fecha: String): String {
    val formatoEntrada = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES"))
    val formatoSalida = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    val fechaDate: Date = formatoEntrada.parse(fecha)
    val fechaFormateada = formatoSalida.format(fechaDate)
    return fechaFormateada.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale(
                "es",
                "ES"
            )
        ) else it.toString()
    }
}

