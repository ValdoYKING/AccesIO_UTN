import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.pixelfusion.accesio_utn.components.ButtonNext
import java.util.Locale
import kotlin.random.Random


fun generateUniqueId(): String {
    return List(10) { Random.nextInt(0, 36).toString(36) }.joinToString("")
}

@Composable
fun CreateScheduleView(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var id by remember { mutableStateOf("") }

    var dia by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var inicia by remember { mutableStateOf("") }
    var termina by remember { mutableStateOf("") }
    var profesor by remember { mutableStateOf("") }
    var salon by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val currentUser = Firebase.auth.currentUser
    val email = currentUser?.email ?: ""

    val dias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
    fun clearFields() {
        dia = ""
        materia = ""
        inicia = ""
        termina = ""
        profesor = ""
        salon = ""
    }
    fun validateFields(schedule: HashMap<String, String>): Boolean {
        if (schedule["dia"].isNullOrBlank() || schedule["materia"].isNullOrBlank() || schedule["inicia"].isNullOrBlank() || schedule["termina"].isNullOrBlank() || schedule["profesor"].isNullOrBlank() || schedule["salon"].isNullOrBlank()) {
            message = "Todos los campos son requeridos"
            return false
        }
        //validate days poniendole la primera letra en mayuscula
        if (!dias.contains(schedule["dia"]?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            })) {
            message = "Dia no valido"
            return false
        }
        return true
    }
    //lista de dias de la semana

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agregar Horario",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = dia,
            onValueChange = { dia = it },
            label = { Text("Dia") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = materia,
            onValueChange = { materia = it },
            label = { Text("Materia") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = inicia,
            onValueChange = { inicia = it },
            label = { Text("Inicia") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = termina,
            onValueChange = { termina = it },
            label = { Text("Termina") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = profesor,
            onValueChange = { profesor = it },
            label = { Text("Profesor") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = salon,
            onValueChange = { salon = it },
            label = { Text("Salon") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val schedule = hashMapOf(

                    "dia" to dia,
                    "materia" to materia,
                    "inicia" to inicia,
                    "termina" to termina,
                    "profesor" to profesor,
                    "salon" to salon,
                    "email" to email
                )

              val response =  validateFields(schedule)
                if(!response ) {
                    return@Button
                }
                //crear un id unico para cada horario con una funcion
                id = generateUniqueId()
                //agregar el email
                schedule["email"] = email
                schedule["id"] = id
                db.collection("horarios").add(schedule)
                    .addOnSuccessListener {
                        message = "Horario actualizado correctamente"
                        clearFields()
                        navController.navigate("home_user_view")
                    }
                    .addOnFailureListener { e ->
                        message = "Error al actualizar horario: $e"
                    }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Guardar")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            color = if (message.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        ButtonNext(navController, "home_user_view")
    }



}

