package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixelfusion.accesio_utn.model.CredentialModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CredentialViewModel:ViewModel() {
    private val _lista : MutableStateFlow<List<CredentialModel>> = MutableStateFlow(emptyList())
    var lista = _lista

    var state by mutableStateOf(CredentialModel())
        private set

    var isLoading by mutableStateOf(false)
        private set

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
        }
    }

    private suspend fun llamarDatosUsuario(){
        val result = withContext(Dispatchers.IO){
            delay(2000)
            listOf(
                CredentialModel("Villalba Mendoza Osvaldo", "232271007", "ESTUDIANTE", "ING. DESARROLLO Y GESTION DE SOFTWARE")
            )
        }
        _lista.value = result
    }


}