import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.pixelfusion.accesio_utn.components.ButtonNext

@Composable
fun EditScheduleView(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var dia by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var inicia by remember { mutableStateOf("") }
    var termina by remember { mutableStateOf("") }
    var profesor by remember { mutableStateOf("") }
    var salon by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    //titulo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Editar Horario",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,


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
                    "salon" to salon
                )
                db.collection("horarios").add(schedule)
                    .addOnSuccessListener {
                        message = "Horario actualizado correctamente"
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
        ButtonNext(navController, "home_user_view")
    }
}