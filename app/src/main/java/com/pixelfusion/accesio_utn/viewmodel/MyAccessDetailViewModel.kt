package com.pixelfusion.accesio_utn.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.AccessUserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyAccessDetailViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    private val _MyHistoryList = MutableStateFlow<List<AccessUserModel>>(emptyList())
    val MyHistoryList: StateFlow<List<AccessUserModel>> = _MyHistoryList

    /*private val _MyHistoryList = mutableStateOf<List<Pair<String, AccessUserModel>>>(emptyList())
    val MyHistoryList: List<Pair<String, AccessUserModel>> get() = _MyHistoryList.value*/


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun fetchData(UidMyAccess: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(1000)
            try {
                if (UidMyAccess != null) {
                    val snapshot = withContext(Dispatchers.IO) {
                        database.child("access_users")
                            .child(UidMyAccess)
                            .get()
                            .await()
                    }

                    val MyHistoryModel = snapshot.getValue(AccessUserModel::class.java)

                    _MyHistoryList.value = MyHistoryModel?.let { listOf(it) } ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}