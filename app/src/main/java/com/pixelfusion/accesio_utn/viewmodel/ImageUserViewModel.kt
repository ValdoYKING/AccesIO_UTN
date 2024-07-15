package com.pixelfusion.accesio_utn.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class ImageUserViewModel : ViewModel() {
    private lateinit var auth: FirebaseAuth
    private val storage = Firebase.storage
    private val database = Firebase.database.reference

    var isLoading by mutableStateOf(false)
        private set

    fun fetchData() {
        viewModelScope.launch {
            try {
                isLoading = true
                //uploadImageAndUpdateUser()
            } catch (e: Exception) {
                println("Error ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun uploadImageAndUpdateUser(
        context: Context,
        imageUri: Uri,
        onSuccess: () -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef =
            storage.reference.child("user_images/${userId}/${imageUri.lastPathSegment}")

        Log.d("ImageUpload", "Uploading to: ${storageRef.path}")

        val uploadTask = storageRef.putFile(imageUri)


        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                database.child("users").child(userId).child("image_path").setValue(uri.toString())
                    .addOnSuccessListener {
                        showToast(context, "Imagen subida y registro actualizado")
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        showToast(context, "Error al actualizar el registro: ${exception.message}")
                    }
            }.addOnFailureListener { exception ->
                showToast(context, "Error al obtener la URL de descarga: ${exception.message}")
            }
        }.addOnFailureListener { exception ->
            showToast(context, "Error al subir la imagen: ${exception.message}")
        }
    }
}