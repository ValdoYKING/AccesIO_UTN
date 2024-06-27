package com.pixelfusion.accesio_utn.model

data class UsuarioData(
    val nombre: String,
    val apellido: String,
    val correo_electronico: String,
    val contrasena: String,
    val fecha_nacimiento: String,
    val fecha_creacion: String,
    val fecha_actualizacion: String,
    val matricula: String,
    val num_seguro_social: String,
    val telefono: String,
    val cuatrimestre: Int,
    val token: String,
    val carrera: String,
    val id_rol: Int

) {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
        "",
        0
    )
}
