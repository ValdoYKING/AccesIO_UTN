package com.pixelfusion.accesio_utn.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.CredentialModel
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class ListQrGenerateViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _qrLugarList = mutableStateOf<List<Pair<String, QrLugarModel>>>(emptyList())
    val qrLugarList: List<Pair<String, QrLugarModel>> get() = _qrLugarList.value

    private val _qrAsistenciaList =
        mutableStateOf<List<Pair<String, QrAsistenciaModel>>>(emptyList())
    val qrAsistenciaList: List<Pair<String, QrAsistenciaModel>> get() = _qrAsistenciaList.value

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
                llamarDatosQrLugar()
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
                    val qrAsistencia = data.getValue(QrAsistenciaModel::class.java)
                    qrAsistencia?.let {
                        Pair(data.key ?: "", it)
                    }
                }
            }

            _qrAsistenciaList.value = result
            _isLoadingAsistencia.value = false
        } ?: run {
            println("Usuario no autenticado")
        }
    }

    private suspend fun llamarDatosQrLugar() {
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
                }
            }

            _qrLugarList.value = result
            _isLoadingLugar.value = false
        } ?: run {
            println("Usuario no autenticado")
        }
    }
}
