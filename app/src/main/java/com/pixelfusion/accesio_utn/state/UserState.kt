package com.pixelfusion.accesio_utn.state

data class UserState(
    val nombre: String = "",
    val apellido: String = "",
    val correo_electronico: String = "",
    val contrasena: String = "",
    val fecha_nacimiento: String = "",
    val fecha_creacion: String = "",
    val fecha_actualizacion: String = "",
    val matricula: String = "",
    val num_seguro_social: String = "",
    val telefono: String = "",
    val cuatrimestre: Int = 0,
    val token: String = "",
    val carrera: String = "",
    val id_rol: Int = 0
)