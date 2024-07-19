package com.pixelfusion.accesio_utn.model

data class AccessUserModel(
    val matricula: String,
    val fecha_access: String,
    val hora_access: String,
    val id_user: String,
    val latitude: Double,
    val longitude: Double
) {
    constructor() : this("", "", "", "", 0.0, 0.0)
}
