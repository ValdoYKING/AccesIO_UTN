package com.pixelfusion.accesio_utn.model

data class HomeModel(
    val nombre : String,
    val matricula : String,
    val tipoUser :String,
    val actualizacion : String

){
    constructor(): this(
        "Licona Valdez Gabriel",
        "232271003",
        "ESTUDIANTE",
        "2024-06-13T20:52:12.756Z"
    )
}

