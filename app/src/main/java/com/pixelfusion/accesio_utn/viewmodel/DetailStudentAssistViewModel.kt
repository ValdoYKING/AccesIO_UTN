package com.pixelfusion.accesio_utn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DetailStudentAssistViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyAssistList = MutableStateFlow<List<QrEstudianteAsisteModel>>(emptyList())
    val MyAssistList: StateFlow<List<QrEstudianteAsisteModel>> = _MyAssistList

    private val _UserDetailsMap = MutableStateFlow<Map<String, UsuarioData>>(emptyMap())
    val UserDetailsMap: StateFlow<Map<String, UsuarioData>> = _UserDetailsMap

    private val _QrAsistenciaMap = MutableStateFlow<Map<String, QrAsistenciaModel>>(emptyMap())
    val QrAsistenciaMap: StateFlow<Map<String, QrAsistenciaModel>> = _QrAsistenciaMap

    private val _isLoading = MutableStateFlow(true) // Comienza en estado de carga
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(uidStudentAsistencia: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = withContext(Dispatchers.IO) {
                    database.child("qr_estudiante_asist")
                        .orderByChild("uid_qr_asistencia")
                        .equalTo(uidStudentAsistencia)
                        .get()
                        .await()
                }

                val myAssistModels = snapshot.children.mapNotNull { data ->
                    data.getValue(QrEstudianteAsisteModel::class.java)
                }

                _MyAssistList.value = myAssistModels

                if (myAssistModels.isEmpty()) {
                    println("No se encontraron registros de QrEstudianteAsisteModel para $uidStudentAsistencia")
                }

                // Fetch related user and QR asistencia details
                myAssistModels.forEach { myAssist ->
                    launch {
                        fetchUserDetails(myAssist.uid_user)
                        fetchQrAsistenciaData(myAssist.uid_qr_asistencia)
                    }
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                // Esperar un breve tiempo para dar oportunidad a que los detalles se obtengan
                delay(2000)
                _isLoading.value = false // Finaliza la carga después de todos los fetch
            }
        }
    }

    suspend fun fetchUserDetails(uidUser: String) {
        try {
            val userSnapshot = withContext(Dispatchers.IO) {
                database.child("users")
                    .child(uidUser)
                    .get()
                    .await()
            }
            val userData = userSnapshot.getValue(UsuarioData::class.java)
            _UserDetailsMap.value = _UserDetailsMap.value.toMutableMap().apply {
                userData?.let { this[uidUser] = it }
            }

            if (userData == null) {
                println("No se encontraron detalles del usuario para $uidUser")
            }
        } catch (e: Exception) {
            println("Error fetching user details: ${e.message}")
        }
    }

    suspend fun fetchQrAsistenciaData(uidQrAsistencia: String) {
        try {
            val snapshot = withContext(Dispatchers.IO) {
                database.child("qr_asistencia")
                    .child(uidQrAsistencia)  // Buscar directamente por el ID del registro
                    .get()
                    .await()
            }

            val qrAsistenciaModel = snapshot.getValue(QrAsistenciaModel::class.java)

            qrAsistenciaModel?.let {
                _QrAsistenciaMap.value = _QrAsistenciaMap.value.toMutableMap().apply {
                    this[uidQrAsistencia] = it
                }
            } ?: run {
                println("No se encontraron detalles de QrAsistenciaModel para $uidQrAsistencia")
            }
        } catch (e: Exception) {
            println("Error fetching QR asistencia details: ${e.message}")
        }
    }
}
