package com.pixelfusion.accesio_utn.viewmodel

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

class QrLugarDetailViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference
    private val storage = Firebase.storage

    private val _qrLugarList = MutableStateFlow<List<QrLugarModel>>(emptyList())
    val qrLugarList: StateFlow<List<QrLugarModel>> = _qrLugarList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(qrUidLugar: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1500)
            try {
                if (qrUidLugar != null) {
                    val snapshot = withContext(Dispatchers.IO) {
                        database.child("qr_lugar")
                            .child(qrUidLugar)
                            .get()
                            .await()
                    }

                    val qrLugarModel = snapshot.getValue(QrLugarModel::class.java)

                    _qrLugarList.value = qrLugarModel?.let { listOf(it) } ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
