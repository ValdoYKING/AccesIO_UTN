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
import com.pixelfusion.accesio_utn.model.ScanQrLugarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class HistoryPlaceViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyHistoryPlaceList =
        mutableStateOf<List<Pair<String, ScanQrLugarModel>>>(emptyList())
    val MyHistoryPlaceList: List<Pair<String, ScanQrLugarModel>> get() = _MyHistoryPlaceList.value

    private val _qrLugarList = mutableStateListOf<Pair<String, QrLugarModel>>()
    val qrLugarList: List<Pair<String, QrLugarModel>> get() = _qrLugarList

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    val _isLoadingMyHistoryPlace = mutableStateOf(true)
    val isLoadingMyHistoryPlace: Boolean get() = _isLoadingMyHistoryPlace.value

    init {
        auth = FirebaseAuth.getInstance()
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                llamarDatosHistorialMyPlace()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun llamarDatosHistorialMyPlace() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("qr_lugar_asist")
                    .orderByChild("uid_user")
                    .equalTo(userId)
                    .get()
                    .await()

                snapshot.children.mapNotNull { data ->
                    val MyPlace = data.getValue(ScanQrLugarModel::class.java)
                    MyPlace?.let {
                        Pair(data.key ?: "", it)
                    }
                }.sortedByDescending { MyPlacePair ->
                    val (uid, MyPlace) = MyPlacePair
                    val fechaHoraStr = "${MyPlace.fecha} ${MyPlace.hora}"
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    LocalDateTime.parse(fechaHoraStr, formatter)
                }
            }
            _MyHistoryPlaceList.value = result
            _isLoadingMyHistoryPlace.value = false
            fetchQrLugarData(result.map { it.second.uid_qr_lugar })
        } ?: run {
            println("Usuario no autenticado")
        }
    }

    private suspend fun fetchQrLugarData(uidList: List<String>) {
        uidList.forEach { uid ->
            val qrLugarSnapshot = database.child("qr_lugar")
                .child(uid)
                .get()
                .await()
            val qrLugar = qrLugarSnapshot.getValue(QrLugarModel::class.java)
            qrLugar?.let {
                _qrLugarList.add(Pair(qrLugarSnapshot.key ?: "", it))
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