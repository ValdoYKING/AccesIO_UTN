package com.pixelfusion.accesio_utn.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.CredentialModel
import com.pixelfusion.accesio_utn.model.HomeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeViewModel: ViewModel() {
    var stateHome by mutableStateOf(HomeModel())
        private set


    private val _lista: MutableStateFlow<List<HomeModel>> = MutableStateFlow(emptyList())
    var lista = _lista


    var isLoading by mutableStateOf(false)
        private set

    private val database = Firebase.database.reference
    private val auth = Firebase.auth

    fun fetchData() {
        viewModelScope.launch {
            try {
                isLoading = true
                llamarDatosUsuarioHome()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun onValue(value:String, key:String){
        when(key){
            "nombre"-> stateHome = stateHome.copy(nombre = value)
            "apellido" -> stateHome = stateHome.copy(apellido = value)
            "matricula"-> stateHome = stateHome.copy(matricula = value)
            "id_rol" -> stateHome = stateHome.copy(id_rol = value)
            "fecha_actualizacion" -> stateHome = stateHome.copy(fecha_actualizacion = value)
            "fecha_creacion" -> stateHome = stateHome.copy(fecha_creacion = value)
        }
    }


    private suspend fun llamarDatosUsuarioHome() {
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val result = withContext(Dispatchers.IO) {
                val snapshot = database.child("users").child(userId).get().await()
                snapshot.getValue(HomeModel::class.java)
            }

            result?.let {
                _lista.value = listOf(it)
                stateHome = it
            } ?: run {
                // Manejar el caso donde los datos no se encontraron
                println("No se encontraron datos para el usuario")
            }
        } ?: run {
            // Manejar el caso donde el usuario no está autenticado
            println("Usuario no autenticado")
        }
    }

    fun longToDateString(timestamp: Long): String {
        // Convert the timestamp to an Instant
        val instant = Instant.ofEpochMilli(timestamp)

        // Create a formatter with a specific pattern and time zone
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())

        // Format the instant to a date string
        return formatter.format(instant)
    }

    /*fun signOut( navController: NavController) {
        // Cerrar sesión en Firebase
        auth.signOut()
        // Navegar a la pantalla de inicio de sesión
        navController.navigate("login_view")
    }*/

    fun logout(navController: NavController) {
        auth.signOut()
        navController.navigate("login_screen") // Ajusta el destino de navegación según sea necesario
    }

    fun salirApp() {
        // Cerrar la aplicación
        System.exit(0)
    }


}