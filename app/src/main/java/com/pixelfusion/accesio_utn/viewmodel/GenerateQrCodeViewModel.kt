package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GenerateQrCodeViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference
    private val storage = Firebase.storage

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
            "materia" -> qrAsistencia = qrAsistencia.copy(materia = value)
            "lugar" -> qrAsistencia = qrAsistencia.copy(lugar = value)
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

    // Método para obtener la latitud y longitud (Ejemplo estático, usa tu lógica real aquí)
    private fun getLocation(): Pair<Double, Double> {
        // Aquí puedes usar la ubicación del dispositivo, un valor predeterminado o algo más
        // Este es un ejemplo estático
        return Pair(19.4326, -99.1332) // Coordenadas para Ciudad de México
    }

    fun registrarQrAsistencia() {
        val user = auth.currentUser
        if (user != null) {
            isLoading = true
            val qrAsistenciaId = database.child("qr_asistencia").push().key
            if (qrAsistenciaId != null) {
                val (date, time) = getCurrentDateTime()
                val (latitude, longitude) = getLocation()

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
                            // Manejo exitoso
                        } else {
                            // Manejo de error
                        }
                    }
            }
        }
    }

    fun registrarQrLugar() {
        val user = auth.currentUser
        if (user != null) {
            isLoading = true
            val qrLugarId = database.child("qr_lugar").push().key
            if (qrLugarId != null) {
                val (date, time) = getCurrentDateTime()
                val (latitude, longitude) = getLocation()

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
                            // Manejo exitoso
                        } else {
                            // Manejo de error
                        }
                    }
            }
        }
    }
}

