package com.pixelfusion.accesio_utn.model

data class UsuarioLoginData(
    val correo_electronico: String,
    val contrasena: String
){
    constructor() : this(
        "",
        ""
    )
}
