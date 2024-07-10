package com.pixelfusion.accesio_utn.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
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

    fun loginUser(navController: NavController, context: Context) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(state.correo_electronico, state.contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("home_user_view")
                } else {
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}