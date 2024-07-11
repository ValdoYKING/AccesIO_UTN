package com.pixelfusion.accesio_utn.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.view.FormRegisterView

class FormRegisterViewModel: ViewModel(){
    var state by mutableStateOf(UsuarioData())
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

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

    fun registerUser(navController: NavController, context: Context) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(state.correo_electronico, state.contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registro exitoso, guarda los datos adicionales en la base de datos
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        val userId = it.uid
                        database.child("users").child(userId).setValue(state)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    // Datos guardados exitosamente, navega a la siguiente pantalla
                                    navController.navigate("legal_screen")
                                } else {
                                    Log.w(TAG, "setValue:failure", dbTask.exception)
                                    Toast.makeText(
                                        context,
                                        "Failed to save user data.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                } else {
                    // Si el registro falla, muestra un mensaje al usuario
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}

