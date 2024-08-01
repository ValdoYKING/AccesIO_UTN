import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.pixelfusion.accesio_utn.model.UsuarioData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*



class UserProfileViewModel : ViewModel() {
    private val _stateHome = MutableStateFlow(UsuarioData())
    val stateHome: StateFlow<UsuarioData> = _stateHome.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    init {
        fetchData()
    }

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

    fun updateUserData(navController: NavController, updatedData: MutableMap<String, Any?>) {
        viewModelScope.launch {
            try {
                isLoading = true
                //updateUserInDatabase(navController, updatedData)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateProfileImage(uri: Uri, navController: NavController) {
        viewModelScope.launch {
            try {
                val imageUrl = uploadImageAndGetUrl(uri)
                Log.d(TAG, "Imagen subida con éxito. URL: $imageUrl")
                //updateUserData(navController, mapOf("image_path" to imageUrl))
            } catch (e: Exception) {
                Log.e(TAG, "Error updating profile image: ${e.message}")
            }
        }
    }

    private suspend fun updateUserInDatabase(navController: NavController, updatedData: Map<String, String>) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val updates = mutableMapOf<String, Any?>()
            for ((key, value) in updatedData) {
                if (value != null) {
                    updates[key] = value
                }
            }
            if (updates.isNotEmpty()) {
                Log.d(TAG, "Datos a actualizar: $updates")
                database.child("users").child(userId).updateChildren(updates).await()
                navController.navigate("perfil_view")
            }
        }
    }

    private suspend fun loadUserData() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val userDataSnapshot = database.child("users").child(userId).get().await()
            val userData = userDataSnapshot.getValue(UsuarioData::class.java)
            _stateHome.value = userData ?: UsuarioData()
        }
    }

    suspend fun uploadImageAndGetUrl(uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }

    fun logout(navController: NavController) {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        navController.navigate("login_screen")
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
