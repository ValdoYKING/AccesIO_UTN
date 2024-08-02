package com.pixelfusion.accesio_utn.view

import UserProfileViewModel
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarDatosView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    val state by viewModelU.stateHome.collectAsState() // Observa los datos del usuario desde el ViewModel
    var numSeguroSocial by remember { mutableStateOf(state.num_seguro_social.orEmpty()) }
    var telefono by remember { mutableStateOf(state.telefono.orEmpty()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModelU.fetchData() // Asegúrate de que los datos del usuario estén cargados
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("profile_view") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = state.nombre ?: "",
                onValueChange = {},
                label = { Text("Nombre") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.apellido ?: "",
                onValueChange = {},
                label = { Text("Apellido") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = state.matricula ?: "",
                onValueChange = {},
                label = { Text("Matrícula") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = numSeguroSocial,
                onValueChange = { numSeguroSocial = it },
                label = { Text("Número de Seguro Social") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val updatedData = mapOf(
                        "num_seguro_social" to numSeguroSocial,
                        "telefono" to telefono
                    )
                    viewModelU.updateUserData(updatedData) {
                        Toast.makeText(
                            context,
                            "Datos modificados exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.navigate("profile_view") {
                            popUpTo("editar_datos_view") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar")
            }
        }
    }
}

