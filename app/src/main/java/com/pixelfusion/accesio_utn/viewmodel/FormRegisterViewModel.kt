package com.pixelfusion.accesio_utn.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.view.FormRegisterView
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FormRegisterViewModel: ViewModel(){
    var state by mutableStateOf(UsuarioData())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    val fecha_creacion = System.currentTimeMillis()


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
            "cuatrimestre" -> state = state.copy(cuatrimestre = value)
            "token"-> state = state.copy(token = value)
            "carrera"-> state = state.copy(carrera = value)
            "id_rol" -> state = state.copy(id_rol = value)
            "image_path" -> state = state.copy(image_path = value)
        }

        state.fecha_creacion = fecha_creacion.toString()

        when (state.carrera) {
            "PERSONAL" -> state.id_rol = "PERSONAL"
            "ADMINISTRATIVO" -> state.id_rol = "ADMINISTRATIVO"
            "DOCENTE" -> state.id_rol = "DOCENTE"
            else -> state.id_rol = "ESTUDIANTE"
        }

    }

    var show by mutableStateOf(false)
        private set

    var showDialog by mutableStateOf(false)

    fun enviar() {
        show = true
    }

    fun openDialog() {
        showDialog = true
    }

    fun closeDialog() {
        showDialog = false
    }

    private suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val snapshot =
                database.child("users").orderByChild("correo_electronico").equalTo(email).get()
                    .await()
            snapshot.exists()
        } catch (e: Exception) {
            Log.w(TAG, "checkEmailExists:failure", e)
            false
        }
    }

    fun fetchData(navController: NavController, context: Context) {
        viewModelScope.launch {
            try {
                isLoading = true
                val emailExists = checkEmailExists(state.correo_electronico)
                if (emailExists) {
                    Toast.makeText(
                        context,
                        "El correo ya está registrado. Usa otro correo.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    registerUser(navController, context)
                }
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun registerUser(navController: NavController, context: Context) {
        auth = Firebase.auth
        try {
            val authResult =
                auth.createUserWithEmailAndPassword(state.correo_electronico, state.contrasena)
                    .await()
            // Registro exitoso, guarda los datos adicionales en la base de datos
            Log.d(TAG, "createUserWithEmail:success")
            val user = authResult.user
            user?.let {
                val userId = it.uid
                database.child("users").child(userId).setValue(state)
                    .addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            // Datos guardados exitosamente, navega a la siguiente pantalla
                            navController.navigate("legal_screen")
                        } else {
                            Log.w(TAG, "setValue:failure", dbTask.exception)
                            Toast.makeText(context, "Failed to save user data.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            // El correo ya está registrado
            Log.w(TAG, "createUserWithEmail:failure", e)
            Toast.makeText(
                context,
                "El correo ya está registrado. Usa otro correo.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            // Otra excepción, error general
            Log.w(TAG, "createUserWithEmail:failure", e)
            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para validar la contraseña
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-zA-Z])(?=.*\\d).{8,}$")
        return passwordPattern.matches(password)
    }

}

