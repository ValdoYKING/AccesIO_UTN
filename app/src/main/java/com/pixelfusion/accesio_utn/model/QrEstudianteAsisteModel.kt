package com.pixelfusion.accesio_utn.model

data class QrEstudianteAsisteModel(
    val titulo: String,
    val tipo: String,
    val duracion: String,
    val division: String,
    val materia: String,
    val lugar: String,
    val uid_qr: String,
    val uid_user: String,
    val fecha: String,
    val hora: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", 0.0, 0.0)
}

