package com.pixelfusion.accesio_utn.model

data class QrAsistenciaModel(
    val titulo: String,
    val duracion: String,
    val division: String,
    val materia: String,
    val lugar: String,
    val fecha: String,
    val hora: String,
    val uid_creo: String,
    val latitude: Double,
    val longitude: Double,
    val descripcion_lugar: String
) {
    constructor() : this("", "", "", "", "", "", "", "", 0.0, 0.0, "")
}
