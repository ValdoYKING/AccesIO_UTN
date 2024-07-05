package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pixelfusion.accesio_utn.model.CredentialModel
import com.pixelfusion.accesio_utn.model.UsuarioData

class CredentialViewModel:ViewModel() {
    var state by mutableStateOf(CredentialModel())
        private set

    fun onValue(value:String, key:String){
        when(key){
            "nombre"-> state = state.copy(nombre = value)
            "matricula"-> state = state.copy(matricula = value)
            "tipouser"-> state = state.copy(tipoUser = value)
            "carrera"-> state = state.copy(carrera = value)
        }
    }


}