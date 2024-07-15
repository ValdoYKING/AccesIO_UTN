package com.pixelfusion.accesio_utn.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.CredentialModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CredentialViewModel:ViewModel() {


    private val _lista : MutableStateFlow<List<CredentialModel>> = MutableStateFlow(emptyList())
    var lista = _lista

    var state by mutableStateOf(CredentialModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    fun fetchData(){
        viewModelScope.launch {
            try{
                isLoading = true
                llamarDatosUsuario()
            }catch (e: Exception){
                println("Error ${e.message}")
            }finally {
                isLoading = false
            }
        }
    }

    fun onValue(value:String, key:String){
        when(key){
            "nombre"-> state = state.copy(nombre = value)
            "matricula"-> state = state.copy(matricula = value)
            "tipouser"-> state = state.copy(tipoUser = value)
            "carrera"-> state = state.copy(carrera = value)
            "image_path" -> state = state.copy(image_path = value)
        }
    }

    private suspend fun llamarDatosUsuario(){
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("users").child(userId).get().await()
                snapshot.getValue(CredentialModel::class.java)
            }

            result?.let {
                _lista.value = listOf(it)
                state = it
            } ?: run {
                // Manejar el caso donde los datos no se encontraron
                println("No se encontraron datos para el usuario")
            }
        } ?: run {
            // Manejar el caso donde el usuario no está autenticado
            println("Usuario no autenticado")
        }
    }





}