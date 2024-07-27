package com.pixelfusion.accesio_utn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.ScanQrLugarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyPlaceDetailViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyPlaceList = MutableStateFlow<List<ScanQrLugarModel>>(emptyList())
    val MyPlaceList: StateFlow<List<ScanQrLugarModel>> = _MyPlaceList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(UidMyPlace: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000)
            try {
                if (UidMyPlace != null) {
                    val snapshot = withContext(Dispatchers.IO) {
                        database.child("qr_lugar_asist")
                            .child(UidMyPlace)
                            .get()
                            .await()
                    }

                    val MyPlaceModel = snapshot.getValue(ScanQrLugarModel::class.java)

                    _MyPlaceList.value = MyPlaceModel?.let { listOf(it) } ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}