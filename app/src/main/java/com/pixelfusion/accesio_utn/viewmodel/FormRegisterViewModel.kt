package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.view.FormRegisterView

class FormRegisterViewModel: ViewModel(){
    var state by mutableStateOf(UsuarioData())
        private set

    fun onValue(value: String, key: String){
        when(key){
            "nombre"-> state = state.copy(nombre = value)
            "apellido"-> state = state.copy(apellido = value)
            "correo_electronico"-> state = state.copy(correo_electronico = value)
            "contrasena"-> state = state.copy(contrasena = value)
            "fecha_nacimiento"-> state = state.copy(fecha_nacimiento = value)
            "fecha_creacion"-> state = state.copy(fecha_creacion = value)
            "fecha_actualizacion"-> state = state.copy(fecha_actualizacion = value)
            "matricula"-> state = state.copy(matricula = value)
            "num_seguro_social"-> state = state.copy(num_seguro_social = value)
            "telefono"-> state = state.copy(telefono = value)
            "cuatrimestre"-> state = state.copy(cuatrimestre = value.toInt())
            "token"-> state = state.copy(token = value)
            "carrera"-> state = state.copy(carrera = value)
            "id_rol"-> state = state.copy(id_rol = value.toInt())
        }

    }

}
