import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {
    var userState by mutableStateOf(UserProfile())

    // Función para manejar el cambio de valores
    fun onValueChange(newValue: String, field: String) {
        userState = when (field) {
            "nombre" -> userState.copy(nombre = newValue)
            "apellido" -> userState.copy(apellido = newValue)
            "matricula" -> userState.copy(matricula = newValue)
            "carrera" -> userState.copy(carrera = newValue)
            "correo_electronico" -> userState.copy(correo_electronico = newValue)
            "num_seguro_social" -> userState.copy(num_seguro_social = newValue)
            else -> userState
        }
    }

    // Función para manejar el cierre de sesión
    fun logout() {
    }
}

data class UserProfile(
    val nombre: String = "",
    val apellido: String = "",
    val matricula: String = "",
    val carrera: String = "",
    val correo_electronico: String = "",
    val num_seguro_social: String = ""
)