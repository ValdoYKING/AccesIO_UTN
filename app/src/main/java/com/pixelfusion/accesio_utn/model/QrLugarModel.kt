package com.pixelfusion.accesio_utn.model

/*Para registrar un qr_lugar*/
data class QrLugarModel(
    val titulo: String,
    val tipo: String,
    val lugar: String,
    val fecha: String,
    val hora: String,
    val uid_creo: String,
    val id_rol: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", "", "", "", "", "", 0.0, 0.0)
}
