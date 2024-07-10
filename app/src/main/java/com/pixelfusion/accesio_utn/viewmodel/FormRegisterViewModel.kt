package com.pixelfusion.accesio_utn.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.view.FormRegisterView

class FormRegisterViewModel: ViewModel(){
    var state by mutableStateOf(UsuarioData())
        private set

    /*private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loading = mutableStateOf(false)*/

    /*private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")*/

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
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(state.correo_electronico, "default_password") // Use a proper password
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                    val database = FirebaseDatabase.getInstance().reference
                    database.child("users").child(userId).setValue(state)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                navController.navigate("main_screen")
                            } else {
                                Toast.makeText(context, "Database error: ${dbTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



}

