import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

data class UsuarioData(
    val nombre: String = "",
    val apellido: String = "",
    val correo_electronico: String = "",
    val telefono: String = "",
    val carrera: String = "",
    val id_rol: String = "",
    val fecha_nacimiento: String = "",
    val num_seguro_social: String = "",
    val image_path: String = "",
    val matricula: String = ""
)

class UserProfileViewModel : ViewModel() {
    var stateHome by mutableStateOf(UsuarioData())
        private set

    var isLoading by mutableStateOf(false)
        private set

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference

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

    fun updateUserData(navController: NavController, updatedData: Map<String, String?>) {
        viewModelScope.launch {
            try {
                isLoading = true
                updateUserInDatabase(navController, updatedData)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating user data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun onValueChange(value: String, key: String) {
        stateHome = when (key) {
            "nombre" -> stateHome.copy(nombre = value)
            "apellido" -> stateHome.copy(apellido = value)
            "correo_electronico" -> stateHome.copy(correo_electronico = value)
            "matricula " -> stateHome.copy(matricula = value)
            "fecha_nacimiento" -> stateHome.copy(fecha_nacimiento = value)
            "num_seguro_social" -> stateHome.copy(num_seguro_social = value)
            "telefono" -> stateHome.copy(telefono = value)
            "image_path" -> stateHome.copy(image_path = value)
            else -> stateHome
        }
    }

    private suspend fun loadUserData() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val userDataSnapshot = database.child("users").child(userId).get().await()
            val userData = userDataSnapshot.getValue(UsuarioData::class.java)
            stateHome = userData ?: UsuarioData()
        }
    }

    private suspend fun updateUserInDatabase(navController: NavController, updatedData: Map<String, String?>) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            val userId = it.uid
            val updates = mutableMapOf<String, Any?>()
            for ((key, value) in updatedData) {
                if (value != null && value != getUserFieldValue(key)) {
                    updates[key] = value
                }
            }
            database.child("users").child(userId).updateChildren(updates).await()
            navController.navigate("perfil_view")
        }
    }
    private fun getUserFieldValue(key: String): String? {
        return when (key) {
            "nombre" -> stateHome.nombre
            "apellido" -> stateHome.apellido
            "correo_electronico" -> stateHome.correo_electronico
            "fecha_nacimiento" -> stateHome.fecha_nacimiento
            "num_seguro_social" -> stateHome.num_seguro_social
            "telefono" -> stateHome.telefono
            "matricula"-> stateHome.matricula
            "image_path" -> stateHome.image_path
            else -> null
        }
    }

    suspend fun uploadImageAndGetUrl(uri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }

    fun updateProfileImage(uri: Uri, navController: NavController) {
        viewModelScope.launch {
            try {
                val imageUrl = uploadImageAndGetUrl(uri)
                onValueChange(imageUrl, "image_path")

                // Crear un mapa con los datos actualizados
                val updatedData = mapOf(
                    "image_path" to imageUrl
                )

                // Pasar updatedData a updateUserData
                updateUserData(navController, updatedData)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating profile image: ${e.message}")
            }
        }
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
