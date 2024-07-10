package com.pixelfusion.accesio_utn.model

import com.pixelfusion.accesio_utn.view.findBestMatch

data class StudentInfo(
    val name: String?,
    val matricula: String?,
    val career: String?
)

fun extractStudentInfo(input: String, careers: List<String>): StudentInfo {
    val nameRegex = """Nombre:\s*([A-Z\s]+)""".toRegex()
    val matriculaRegex = """Matricula:\s*(\d+)""".toRegex()

    val nameMatch = nameRegex.find(input)
    val matriculaMatch = matriculaRegex.find(input)

    val name = nameMatch?.groups?.get(1)?.value?.trim()
    val matricula = matriculaMatch?.groups?.get(1)?.value?.trim()

    val career = findBestMatch(input, careers)

    return StudentInfo(name, matricula, career)
}

