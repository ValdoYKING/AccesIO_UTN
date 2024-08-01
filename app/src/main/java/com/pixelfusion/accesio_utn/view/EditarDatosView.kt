package com.pixelfusion.accesio_utn.view

import UserProfileViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditarDatosView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    val state by viewModelU.stateHome.collectAsState()
    var nombre by remember { mutableStateOf(state.nombre) }
    var apellido by remember { mutableStateOf(state.apellido) }
    var correoElectronico by remember { mutableStateOf(state.correo_electronico) }
    var matricula by remember { mutableStateOf(state.matricula) }
    var fechaNacimiento by remember { mutableStateOf(state.fecha_nacimiento) }
    var numSeguroSocial by remember { mutableStateOf(state.num_seguro_social) }
    var telefono by remember { mutableStateOf(state.telefono) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = correoElectronico,
                onValueChange = { correoElectronico = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = { Text("Matrícula") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = fechaNacimiento,
                onValueChange = { fechaNacimiento = it },
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = numSeguroSocial,
                onValueChange = { numSeguroSocial = it },
                label = { Text("Número de Seguro Social") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Crear un mapa con los datos actualizados solo si son diferentes
                    val updatedData = mutableMapOf<String, Any?>().apply {
                        if (nombre != state.nombre) put("nombre", nombre)
                        if (apellido != state.apellido) put("apellido", apellido)
                        if (correoElectronico != state.correo_electronico) put("correo_electronico", correoElectronico)
                        if (matricula != state.matricula) put("matricula", matricula)
                        if (fechaNacimiento != state.fecha_nacimiento) put("fecha_nacimiento", fechaNacimiento)
                        if (numSeguroSocial != state.num_seguro_social) put("num_seguro_social", numSeguroSocial)
                        if (telefono != state.telefono) put("telefono", telefono)
                    }

                    // Actualizar los datos del usuario si hay campos modificados
                    if (updatedData.isNotEmpty()) {
                        viewModelU.updateUserData(navController, updatedData)
                    } else {
                        // Si no hay campos modificados, regresar a la vista de perfil
                        navController.navigate("perfil_view")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}
