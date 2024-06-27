package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pixelfusion.accesio_utn.model.UsuarioLoginData

class LoginViewModel: ViewModel() {
    var state by mutableStateOf(UsuarioLoginData())
        private set

    fun onValue(value: String, key: String) {
        when (key) {
            "correo_electronico" -> state = state.copy(correo_electronico = value)
            "contrasena" -> state = state.copy(contrasena = value)
        }
    }

    fun login(correo_electronico: String, contrasena: String): Boolean {
        return correo_electronico == "admin" && contrasena == "admin"
    }
}