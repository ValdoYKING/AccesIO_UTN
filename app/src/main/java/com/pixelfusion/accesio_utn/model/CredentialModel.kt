package com.pixelfusion.accesio_utn.model

data class CredentialModel(
    val nombre : String,
    val matricula : String,
    val tipoUser :String,
    val carrera : String
){
    constructor(): this(
        "Villalba Mendoza Osvaldo",
        "232271007",
        "ESTUDIANTE",
        "ING, EN DESARROLLO Y GESTION DE SOFTWARE"
    )
}


