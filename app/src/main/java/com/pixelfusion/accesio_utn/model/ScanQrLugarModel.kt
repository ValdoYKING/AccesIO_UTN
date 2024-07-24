package com.pixelfusion.accesio_utn.model

data class ScanQrLugarModel(
    val titulo: String,
    val tipo: String,
    val lugar: String,
    val uid_qr_lugar: String,
    val uid_user: String,
    val fecha: String,
    val hora: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", "", "", "", "", "", 0.0, 0.0)
}
