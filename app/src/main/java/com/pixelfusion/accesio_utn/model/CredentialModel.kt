package com.pixelfusion.accesio_utn.model

data class CredentialModel(
    val nombre : String,
    val matricula : String,
    val tipoUser :String,
    val carrera: String,
    val image_path: String
){
    constructor(): this(
        "",
        "",
        "",
        "",
        ""
    )
}


