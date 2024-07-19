package com.pixelfusion.accesio_utn.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScanQRAccessViewModel : ViewModel() {
    var state by mutableStateOf(AccessUserModel())
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference
    private val storage = Firebase.storage

    var user by mutableStateOf(UsuarioData())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private var _isLoadingDialog by mutableStateOf(false)
    val isLoadingDialog: Boolean get() = _isLoadingDialog

    private var _isUserDetailDialogVisible by mutableStateOf(false)
    val isUserDetailDialogVisible: Boolean get() = _isUserDetailDialogVisible

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun updateQrCodeContent(qrContent: String) {
        val regex = """(\d{2}:\d{2}:\d{2}),(\d{2}-\d{2}-\d{4}),(\d{9}),([a-zA-Z0-9]+)""".toRegex()
        val matchResult = regex.find(qrContent)
        if (matchResult != null) {
            val (hora, fecha, matricula, uid) = matchResult.destructured
            state = AccessUserModel(
                matricula = matricula,
                fecha_access = fecha,
                hora_access = hora,
                id_user = uid
            )
            insertAccessUserData()
            fetchUsuarioData(uid)
        } else {
            state = AccessUserModel()
            println("El formato del QR no coincide con el esperado")
        }
    }

    private fun insertAccessUserData() {
        val newAccessUserRef = database.child("access_users").push()
        newAccessUserRef.setValue(state)
    }

    fun fetchUsuarioData(id_user: String) {
        isLoading = true
        val userRef = database.child("users").child(id_user)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    val usuarioData = dataSnapshot.getValue(UsuarioData::class.java)
                    if (usuarioData != null) {
                        user = usuarioData
                        _isLoadingDialog = true

                        // Iniciar una corrutina para cerrar el indicador de progreso después de 2 segundos
                        viewModelScope.launch {
                            delay(1000L)
                            _isLoadingDialog = false
                            _isUserDetailDialogVisible = true

                            // Cerrar el AlertDialog después de otros 2 segundos
                            delay(3000L)
                            _isUserDetailDialogVisible = false
                        }
                    } else {
                        println("No se encontraron datos para el usuario con id: $id_user")
                    }
                } catch (e: DatabaseException) {
                    println("Error al deserializar los datos del usuario: ${e.message}")
                } finally {
                    isLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error
            }
        })
    }

    fun dismissUserDetailDialog() {
        _isUserDetailDialogVisible = false
    }

    fun showUserDetailDialog(visible: Boolean) {
        _isUserDetailDialogVisible = visible
    }
}


