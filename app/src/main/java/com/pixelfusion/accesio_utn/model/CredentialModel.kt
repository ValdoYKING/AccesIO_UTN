package com.pixelfusion.accesio_utn.model

data class CredentialModel(
    val nombre : String,
    val apellido: String,
    val matricula : String,
    val id_rol: String,
    val carrera: String,
    val image_path: String,
    val num_seguro_social: String?,
    val telefono: String?
){




    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",


    )
}


