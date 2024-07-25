package com.pixelfusion.accesio_utn.model

data class QrEstudianteAsisteModel(
    val uid_qr_asistencia: String,
    val uid_user: String,
    val fecha: String,
    val hora: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", "", "", 0.0, 0.0)
}

