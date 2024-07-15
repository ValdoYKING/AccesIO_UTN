package com.pixelfusion.accesio_utn.model

data class UsuarioData(
    val nombre: String,
    val apellido: String,
    val correo_electronico: String,
    val contrasena: String,
    val fecha_nacimiento: String,
    var fecha_creacion: String,
    val fecha_actualizacion: String,
    val matricula: String,
    val num_seguro_social: String,
    val telefono: String,
    val cuatrimestre: String,
    val token: String,
    val carrera: String,
    var id_rol: String,
    val image_path: String

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
        "",
        "",
        "",
        "",
        ""
    )
}
