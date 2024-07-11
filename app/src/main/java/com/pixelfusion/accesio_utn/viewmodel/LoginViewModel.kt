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
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.model.UsuarioLoginData

class LoginViewModel: ViewModel() {
    var state by mutableStateOf(UsuarioLoginData())
        private set

    private lateinit var auth: FirebaseAuth

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
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(state.correo_electronico, state.contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    // Aquí puedes manejar la navegación después del inicio de sesión exitoso
                    navController.navigate("legal_screen")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


}