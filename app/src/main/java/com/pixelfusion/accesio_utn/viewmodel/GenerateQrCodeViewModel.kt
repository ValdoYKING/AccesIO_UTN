package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GenerateQrCodeViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    var qrAsistencia by mutableStateOf(QrAsistenciaModel())
        private set

    var qrLugar by mutableStateOf(QrLugarModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var _isLoadingDialog by mutableStateOf(false)
    val isLoadingDialog: Boolean get() = _isLoadingDialog

    private var _isUserDetailDialogVisible by mutableStateOf(false)
    val isUserDetailDialogVisible: Boolean get() = _isUserDetailDialogVisible

    private val _latitude = mutableStateOf(0.0)
    val latitude: Double get() = _latitude.value
    private val _longitude = mutableStateOf(0.0)
    val longitude: Double get() = _longitude.value

    // Suggestions lists
    val suggestionsQR = listOf("Asistencia", "Lugar")
    val suggestionsAsistenciaDivision = listOf(
        "División de Informática y Computación",
        "División de Gestión de la Producción",
        "División de Comercialización",
        "División de Administración",
        "División de Tecnología Ambiental",
        "División de Telemática",
        "División de Aviónica" ,
        "División de Chimalhuacán"
    )
    val suggestioncarreras = listOf(
        "TSU en TI Infraestructura de Redes Digitales",
        "TSU en TI Desarrollo de Software Multiplataforma",
        "TSU en Mecatrónica Área Sistemas de Manufactura Flexible",
        "TSU en Desarrollo de Negocios Área Mercadotecnia",
        "TSU en Química Área Ambiental",
        "TSU en TI Entornos Virtuales y Negocios Digitales",
        "TSU en Administración Área Capital Humano",
        "TSU en Procesos Industriales Área Manufactura",
        "TSU en Mantenimiento Aeronáutico Área Aviónica",
        "ING. EN MECATRÓNICA",
        "ING. EN TECNOLOGIÁS DE LA PRODUCCIÓN",
        "ING. EN DESARROLLO Y GESTIÓN DE SOFTWARE",
        "ING. EN REDES INTELIGENTES Y CIBERSEGURIDAD",
        "ING. EN ENTORNOS VIRTUALES Y NEGOCIOS",
        "ING. EN TECNOLOGÍA AMBIENTAL",
        "LIC. EN GESTIÓN DEL CAPITAL HUMANO",
        "LIC EN INNOVACIÓN DE NEGOCIOS Y MERCADOTECNIA",
        "MAESTRÍA EN GESTIÓN E INNOVACIÓN DE LAS ORGANIZACIONES",
        "MAESTRÍA EN SISTEMAS DE GESTIÓN AMBIENTAL",
        "MAESTRÍA EN INNOVACIÓN Y NEGOCIOS"
    )

    val cuatrimestre = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val suggestionMateria = mapOf(

        "1" to listOf(
        "Álgebra Lineal",
        "Desarrollo de habilidades del pensamiento lógico",
        "Fundamentos de TI",
        "Fundamentos de Redes",
        "Metodología de la Programación",
        "Inglés I",
        "Expresión Oral y Escrita I",
        "Formación Sociocultural I"
    ),

    "2" to  listOf(
        "Funciones Matemáticas",
        "Metodologías y modelado de desarrollo de software",
        "Interconexión de Redes",
        "Introducción al diseño digital",
        "Programación Orientada a Objetos",
        "Base de datos",
        "Inglés II",
        "Formación Sociocultural II"
    ),

    "3" to  listOf(
        "Cálculo diferencial",
        "Sistemas operativos",
        "Conmutación en redes de datos",
        "Infraestructura de redesde datos",
        "Aplicaciones web",
        "Integradora I",
        "Inglés III",
        "Formación Sociocultural III"
    ),
    "4" to  listOf("MATERIA 1", "MATERIA 2", "MATERIA 3")
    )

    fun getMateriasPorCuatrimestre(cuatrimestre: String?): List<String> {
        return suggestionMateria[cuatrimestre] ?: emptyList()
    }

    val suggestionsAsistenciaDuracion = listOf("1", "2", "3", "4", "5")
    val suggestionsAsistenciaLugar = listOf("Laboratorio", "Salon")
    val suggestionsLugar = listOf(
        "Edificio",
        "Salon",
        "Laboratorio",
        "Cubiculo",
        "Otro"
    )

    val suggestionsLugarEdificio = listOf(
        "Biblioteca",
        "Servicios escolares",
        "Gimnasio",
        "Piscina",
        "Estadio",
    )
    val suggestionsLugarSalon = listOf("101", "102", "103", "104", "105")
    val suggestionsLugarLaboratorio = listOf(
        "Laboratorio de informatica",
        "Laboratorio de telematica",
    )

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun onValueLugar(value: String, key: String) {
        when (key) {
            "titulo" -> qrLugar = qrLugar.copy(titulo = value)
            "tipo" -> qrLugar = qrLugar.copy(tipo = value)
            "lugar" -> qrLugar = qrLugar.copy(lugar = value)
            "fecha" -> qrLugar = qrLugar.copy(fecha = value)
            "hora" -> qrLugar = qrLugar.copy(hora = value)
            "uid_creo" -> qrLugar = qrLugar.copy(uid_creo = value)
            //"id_rol" -> qrLugar = qrLugar.copy(id_rol = value)
            "latitude" -> qrLugar = qrLugar.copy(latitude = value.toDouble())
            "longitude" -> qrLugar = qrLugar.copy(longitude = value.toDouble())
        }
    }


    fun onValueAsistencia(value: String, key: String) {
        when (key) {
            "titulo" -> qrAsistencia = qrAsistencia.copy(titulo = value)
            "duracion" -> qrAsistencia = qrAsistencia.copy(duracion = value)
            "division" -> qrAsistencia = qrAsistencia.copy(division = value)
            "carrera" -> qrAsistencia = qrAsistencia.copy(carrera = value)
            "materia" -> qrAsistencia = qrAsistencia.copy(materia = value)
            "lugar" -> qrAsistencia = qrAsistencia.copy(lugar = value)
            "cuatrimestre" -> qrAsistencia = qrAsistencia.copy(cuatrimestre = value)
            "descripcion_lugar" -> qrAsistencia = qrAsistencia.copy(descripcion_lugar = value)
            "fecha" -> qrAsistencia = qrAsistencia.copy(fecha = value)
            "hora" -> qrAsistencia = qrAsistencia.copy(hora = value)
            "uid_creo" -> qrAsistencia = qrAsistencia.copy(uid_creo = value)
            "latitude" -> qrAsistencia = qrAsistencia.copy(latitude = value.toDouble())
            "longitude" -> qrAsistencia = qrAsistencia.copy(longitude = value.toDouble())
        }
    }

    // Método para obtener la fecha y hora actual con segundos
    private fun getCurrentDateTime(): Pair<String, String> {
        val formatterDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formatterTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = formatterDate.format(Date())
        val time = formatterTime.format(Date())
        return Pair(date, time)
    }

    fun updateLocation(latitude: Double, longitude: Double) {
        _latitude.value = latitude
        _longitude.value = longitude
    }

    fun registrarQrAsistencia(navController: NavController) {
        val user = auth.currentUser
        if (user != null) {
            isLoading = true
            val qrAsistenciaId = database.child("qr_asistencia").push().key
            if (qrAsistenciaId != null) {
                _isLoadingDialog = true
                val (date, time) = getCurrentDateTime()
                val latitude = _latitude.value ?: 0.0
                val longitude = _longitude.value ?: 0.0

                // Iniciar una corrutina para cerrar el indicador de progreso después de 2 segundos
                viewModelScope.launch {
                    delay(5000L)
                    _isLoadingDialog = false
                    //_isUserDetailDialogVisible = true

                    // Cerrar el AlertDialog después de otros 2 segundos
                    /*delay(3000L)
                    _isUserDetailDialogVisible = false*/
                }

                val qrAsistenciaData = qrAsistencia.copy(
                    uid_creo = user.uid,
                    fecha = date,
                    hora = time,
                    latitude = latitude,
                    longitude = longitude
                )
                database.child("qr_asistencia").child(qrAsistenciaId).setValue(qrAsistenciaData)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            navController.navigate("lista_mi_qr")
                        } else {
                            // Manejo de error
                        }
                    }
            }
        }
    }

    fun registrarQrLugar(navController: NavController) {
        val user = auth.currentUser
        if (user != null) {
            isLoading = true
            val qrLugarId = database.child("qr_lugar").push().key
            if (qrLugarId != null) {
                _isLoadingDialog = true
                val (date, time) = getCurrentDateTime()
                val latitude = _latitude.value ?: 0.0
                val longitude = _longitude.value ?: 0.0

                // Iniciar una corrutina para cerrar el indicador de progreso después de 5 segundos
                viewModelScope.launch {
                    delay(5000L)
                    _isLoadingDialog = false
                }

                val qrLugarData = qrLugar.copy(
                    uid_creo = user.uid,
                    fecha = date,
                    hora = time,
                    latitude = latitude,
                    longitude = longitude
                )
                database.child("qr_lugar").child(qrLugarId).setValue(qrLugarData)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            navController.navigate("lista_mi_qr")
                        } else {
                            // Manejo de error
                        }
                    }
            }
        }
    }
}