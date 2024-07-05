package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pixelfusion.accesio_utn.model.HomeModel

class HomeViewModel: ViewModel() {
    var stateHome by mutableStateOf(HomeModel())
        private set

    fun onValue(value:String, key:String){
        when(key){
            "nombre"-> stateHome = stateHome.copy(nombre = value)
            "matricula"-> stateHome = stateHome.copy(matricula = value)
            "tipoUser"-> stateHome = stateHome.copy(tipoUser = value)
            "actualizacion"-> stateHome = stateHome.copy(actualizacion = value)
        }
    }
}