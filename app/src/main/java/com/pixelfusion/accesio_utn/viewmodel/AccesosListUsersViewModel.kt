package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.AccessUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class AccesosListUsersViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyHistoryList = mutableStateOf<List<Pair<String, AccessUserModel>>>(emptyList())
    val MyHistoryList: List<Pair<String, AccessUserModel>> get() = _MyHistoryList.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    val _isLoadingMyHistory = mutableStateOf(true)
    val isLoadingMyHistory: Boolean get() = _isLoadingMyHistory.value

    init {
        auth = FirebaseAuth.getInstance()
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                llamarDatosHistorialMyAccess()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun llamarDatosHistorialMyAccess() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("access_users")
                    .orderByChild("id_user")
                    //.equalTo(userId)
                    .get()
                    .await()

                snapshot.children.mapNotNull { data ->
                    val MyAccess = data.getValue(AccessUserModel::class.java)
                    MyAccess?.let {
                        Pair(data.key ?: "", it)
                    }
                }.sortedByDescending { MyAccessPair ->
                    val (uid, MyAccess) = MyAccessPair
                    // Combinar fecha y hora en un solo LocalDateTime para ordenar
                    val fechaHoraStr = "${MyAccess.fecha_access} ${MyAccess.hora_access}"
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                    LocalDateTime.parse(fechaHoraStr, formatter)
                }
            }
            _MyHistoryList.value = result
            _isLoadingMyHistory.value = false
        } ?: run {
            println("Usuario no autenticado")
        }
    }

    fun determinarTipoDeRegistro(fechaAccess: String, horaAccess: String): String {
        val registrosDelDia = _MyHistoryList.value.filter {
            it.second.fecha_access == fechaAccess
        }.sortedBy {
            val fechaHoraStr = "${it.second.fecha_access} ${it.second.hora_access}"
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            LocalDateTime.parse(fechaHoraStr, formatter)
        }

        val index = registrosDelDia.indexOfFirst { it.second.hora_access == horaAccess }

        return if (index % 2 == 0) "Entrada" else "Salida"
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