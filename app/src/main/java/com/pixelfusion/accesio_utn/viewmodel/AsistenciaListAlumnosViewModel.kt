package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class AsistenciaListAlumnosViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _qrLugarList = mutableStateOf<List<Pair<String, QrLugarModel>>>(emptyList())
    val qrLugarList: List<Pair<String, QrLugarModel>> get() = _qrLugarList.value

    private val _AsistenciaUserList =
        mutableStateOf<List<Pair<String, QrAsistenciaModel>>>(emptyList())
    val AsistenciaUserList: List<Pair<String, QrAsistenciaModel>> get() = _AsistenciaUserList.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    val _isLoadingAsistencia = mutableStateOf(true)
    val isLoadingAsistencia: Boolean get() = _isLoadingAsistencia.value

    val _isLoadingLugar = mutableStateOf(true)
    val isLoadingLugar: Boolean get() = _isLoadingLugar.value

    init {
        auth = FirebaseAuth.getInstance()
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                llamarDatosQrAsistencia()
                //llamarDatosQrLugar()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun llamarDatosQrAsistencia() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("qr_asistencia")
                    .orderByChild("uid_creo")
                    .equalTo(userId)
                    .get()
                    .await()

                snapshot.children.mapNotNull { data ->
                    val AsistenciaUser = data.getValue(QrAsistenciaModel::class.java)
                    AsistenciaUser?.let {
                        Pair(data.key ?: "", it)
                    }
                }.sortedByDescending { AsistenciaUserPair ->
                    val (uid, AsistenciaUser) = AsistenciaUserPair
                    // Combinar fecha y hora en un solo LocalDateTime para ordenar
                    val fechaHoraStr = "${AsistenciaUser.fecha} ${AsistenciaUser.hora}"
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    LocalDateTime.parse(fechaHoraStr, formatter)
                }
            }

            _AsistenciaUserList.value = result
            _isLoadingAsistencia.value = false
        } ?: run {
            println("Usuario no autenticado")
        }
    }

    /*private suspend fun llamarDatosQrLugar() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("qr_lugar")
                    .orderByChild("uid_creo")
                    .equalTo(userId)
                    .get()
                    .await()

                snapshot.children.mapNotNull { data ->
                    val qrLugar = data.getValue(QrLugarModel::class.java)
                    qrLugar?.let {
                        Pair(data.key ?: "", it)
                    }
                }.sortedByDescending { qrLugarPair ->
                    val (uid, qrLugar) = qrLugarPair
                    // Combinar fecha y hora en un solo LocalDateTime para ordenar
                    val fechaHoraStr = "${qrLugar.fecha} ${qrLugar.hora}"
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    LocalDateTime.parse(fechaHoraStr, formatter)
                }
            }

            _qrLugarList.value = result
            _isLoadingLugar.value = false
        } ?: run {
            println("Usuario no autenticado")
        }
    }*/


    /*fun calcularNuevaFechaYHora(fecha: String, hora: String, duracion: String): String {
        val duracionEnHoras = duracion.toIntOrNull() ?: throw IllegalArgumentException("Duración inválida")
        val fechaHoraStr = "$fecha $hora"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

        // Parsear la fecha y hora inicial
        val fechaHoraInicial = LocalDateTime.parse(fechaHoraStr, formatter)

        // Sumar la duración en horas
        val nuevaFechaHora = fechaHoraInicial.plus(duracionEnHoras.toLong(), ChronoUnit.HOURS)

        // Formatear la nueva fecha y hora
        return nuevaFechaHora.format(formatter)
    }*/


    fun calcularNuevaFechaYHora(fecha: String, hora: String, duracion: String): LocalDateTime {
        val duracionEnHoras =
            duracion.toIntOrNull() ?: throw IllegalArgumentException("Duración inválida")
        val fechaHoraStr = "$fecha $hora"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

        // Parsear la fecha y hora inicial
        val fechaHoraInicial = LocalDateTime.parse(fechaHoraStr, formatter)

        // Sumar la duración en horas
        return fechaHoraInicial.plus(duracionEnHoras.toLong(), ChronoUnit.HOURS)
    }
}