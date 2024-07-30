package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccessDetailUserViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _HistoryListUser = MutableStateFlow<List<AccessUserModel>>(emptyList())
    val HistoryListUser: StateFlow<List<AccessUserModel>> = _HistoryListUser

    private val _MyHistoryList = mutableStateOf<List<Pair<String, AccessUserModel>>>(emptyList())
    val MyHistoryList: List<Pair<String, AccessUserModel>> get() = _MyHistoryList.value

    private val _UserData = MutableStateFlow<UsuarioData?>(null)
    val UserData: StateFlow<UsuarioData?> = _UserData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    // Función para cargar los dos modelos
    fun fetchData(UidAccessUser: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (UidAccessUser != null) {
                    // Fetch AccessUserModel data
                    val accessSnapshot = withContext(Dispatchers.IO) {
                        database.child("access_users")
                            .child(UidAccessUser)
                            .get()
                            .await()
                    }

                    val accessUserModel = accessSnapshot.getValue(AccessUserModel::class.java)
                    _HistoryListUser.value = accessUserModel?.let { listOf(it) } ?: emptyList()

                    // Obtener el id_user desde AccessUserModel
                    val idUser = accessUserModel?.id_user ?: ""

                    // Fetch UsuarioData (usando la función loadUserData)
                    loadUserData(idUser)

                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para obtener los datos del usuario con el id_user
    private suspend fun loadUserData(idUser: String) {
        if (idUser.isNotEmpty()) {
            val userDataSnapshot = database.child("users").child(idUser).get().await()
            val userData = userDataSnapshot.getValue(UsuarioData::class.java)
            _UserData.value = userData
        }
    }

    fun determinarTipoDeAccesoUsuario(fechaAccess: String, horaAccess: String): String {
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

}