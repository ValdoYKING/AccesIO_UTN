package com.pixelfusion.accesio_utn.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserProfileViewModel : ViewModel() {

    // Estado mutable para almacenar los datos del usuario
    var stateHome by mutableStateOf(UsuarioData())
        private set

    // Estado mutable para indicar si se está cargando algún proceso
    var isLoading by mutableStateOf(false)
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

    // Función para cargar los datos del usuario
    fun fetchData() {
        viewModelScope.launch {
            try {
                isLoading = true
                loadUserData()
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching user data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Función para actualizar los valores del usuario
    fun updateUserData(navController: NavController) {
        viewModelScope.launch {
            try {
                isLoading = true
                updateUserInDatabase(navController)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // Función para manejar los cambios en los valores del usuario
    fun onValueChange(value: String, key: String) {
        stateHome = when (key) {
            "nombre" -> stateHome.copy(nombre = value)
            "apellido" -> stateHome.copy(apellido = value)
            "correo_electronico" -> stateHome.copy(correo_electronico = value)
            "contrasena" -> stateHome.copy(contrasena = value)
            "fecha_nacimiento" -> stateHome.copy(fecha_nacimiento = value)
            "matricula" -> stateHome.copy(matricula = value)
            "num_seguro_social" -> stateHome.copy(num_seguro_social = value)
            "telefono" -> stateHome.copy(telefono = value)
            "cuatrimestre" -> stateHome.copy(cuatrimestre = value)
            "token" -> stateHome.copy(token = value)
            "carrera" -> stateHome.copy(carrera = value)
            "id_rol" -> stateHome.copy(id_rol = value)
            "image_path" -> stateHome.copy(image_path = value)
            else -> stateHome
        }
    }

    // Función para cargar los datos del usuario desde Firebase
    private suspend fun loadUserData() {
        auth = Firebase.auth
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val userDataSnapshot = database.child("users").child(userId).get().await()
            val userData = userDataSnapshot.getValue(UsuarioData::class.java)
            stateHome = userData ?: UsuarioData() // Actualiza el estado con los datos obtenidos
        }
    }

    // Función para actualizar los datos del usuario en la base de datos Firebase
    private suspend fun updateUserInDatabase(navController: NavController) {
        auth = Firebase.auth
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            database.child("users").child(userId).setValue(stateHome)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User data updated successfully")
                        navController.navigate("profile_updated_screen")
                    } else {
                        Log.e(TAG, "Failed to update user data", task.exception)
                        // Manejar el fallo al actualizar datos
                    }
                }
        }
    }



    // Función para cerrar sesión
    fun logout(navController: NavController) {
        auth = Firebase.auth
        auth.signOut()
        navController.navigate("login_screen") // Ajusta el destino de navegación según sea necesario
    }



    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
