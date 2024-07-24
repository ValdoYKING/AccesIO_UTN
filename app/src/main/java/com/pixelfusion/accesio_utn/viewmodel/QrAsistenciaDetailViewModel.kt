package com.pixelfusion.accesio_utn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class QrAsistenciaDetailViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference
    private val storage = Firebase.storage

    private val _qrAsistenciaList = MutableStateFlow<List<QrAsistenciaModel>>(emptyList())
    val qrAsistenciaList: StateFlow<List<QrAsistenciaModel>> = _qrAsistenciaList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(qrUidAsistencia: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(300)
            try {
                if (qrUidAsistencia != null) {
                    val snapshot = withContext(Dispatchers.IO) {
                        database.child("qr_asistencia")
                            .child(qrUidAsistencia)
                            .get()
                            .await()
                    }

                    val qrAsistenciaModel = snapshot.getValue(QrAsistenciaModel::class.java)

                    _qrAsistenciaList.value = qrAsistenciaModel?.let { listOf(it) } ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
