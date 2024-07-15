package com.pixelfusion.accesio_utn.model

data class HomeModel(
    val nombre : String,
    val apellido: String,
    val matricula : String,
    val id_rol: String,
    val fecha_actualizacion: String,
    val fecha_creacion: String,
    val cuatrimestre: String

){
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}

