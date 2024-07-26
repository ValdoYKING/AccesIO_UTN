package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class HistoryMyAssistViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyHistoryAssistList =
        mutableStateOf<List<Pair<String, QrEstudianteAsisteModel>>>(emptyList())
    val MyHistoryAssistList: List<Pair<String, QrEstudianteAsisteModel>> get() = _MyHistoryAssistList.value

    private val _qrAsistenciaList = mutableStateListOf<Pair<String, QrAsistenciaModel>>()
    val qrAsistenciaList: List<Pair<String, QrAsistenciaModel>> get() = _qrAsistenciaList

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    val _isLoadingMyHistoryAssist = mutableStateOf(true)
    val isLoadingMyHistoryAssist: Boolean get() = _isLoadingMyHistoryAssist.value

    init {
        auth = FirebaseAuth.getInstance()
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                llamarDatosHistorialMyAssist()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun llamarDatosHistorialMyAssist() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("qr_estudiante_asist")
                    .orderByChild("uid_user")
                    .equalTo(userId)
                    .get()
                    .await()

                snapshot.children.mapNotNull { data ->
                    val MyAssist = data.getValue(QrEstudianteAsisteModel::class.java)
                    MyAssist?.let {
                        Pair(data.key ?: "", it)
                    }
                }.sortedByDescending { MyAssistPair ->
                    val (uid, MyAssist) = MyAssistPair
                    val fechaHoraStr = "${MyAssist.fecha} ${MyAssist.hora}"
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    LocalDateTime.parse(fechaHoraStr, formatter)
                }
            }
            _MyHistoryAssistList.value = result
            _isLoadingMyHistoryAssist.value = false
            fetchQrAsistenciaData(result.map { it.second.uid_qr_asistencia })
        } ?: run {
            println("Usuario no autenticado")
        }
    }

    private suspend fun fetchQrAsistenciaData(uidList: List<String>) {
        uidList.forEach { uid ->
            val qrAsistenciaSnapshot = database.child("qr_asistencia")
                .child(uid)
                .get()
                .await()
            val qrAsistencia = qrAsistenciaSnapshot.getValue(QrAsistenciaModel::class.java)
            qrAsistencia?.let {
                _qrAsistenciaList.add(Pair(qrAsistenciaSnapshot.key ?: "", it))
            }
        }
    }

    fun convertirFechaATexto(fecha: String): String {
        val formatoEntrada = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES"))
        val formatoSalida = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
        val fechaDate: Date = formatoEntrada.parse(fecha)
        val fechaFormateada = formatoSalida.format(fechaDate)
        return fechaFormateada.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale(
                    "es",
                    "ES"
                )
            ) else it.toString()
        }
    }

}