package com.pixelfusion.accesio_utn.view

import ContenidoSuperiorWithTitle
import UserProfileViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditarDatosView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    var email by remember { mutableStateOf(viewModelU.stateHome.correo_electronico) }
    var fechaNacimiento by remember { mutableStateOf(viewModelU.stateHome.fecha_nacimiento) }
    var telefono by remember { mutableStateOf(viewModelU.stateHome.telefono) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirmar Cambios") },
            text = { Text("¿Estás seguro de que quieres actualizar tus datos?") },
            confirmButton = {
                Button(
                    onClick = {
                        val updatedData = mapOf(
                            "correo_electronico" to email,
                            "fecha_nacimiento" to fechaNacimiento,
                            "telefono" to telefono
                        )
                        viewModelU.updateUserData(navController, updatedData)
                        showConfirmationDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancelar") },
            text = { Text("¿Estás seguro de que quieres cancelar los cambios?") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate("perfil_view") {
                            popUpTo("editar_datos_view") { inclusive = true }
                        }
                        showCancelDialog = false
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showCancelDialog = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            ContenidoSuperiorWithTitle(
                drawerState = rememberDrawerState(DrawerValue.Closed),
                scope = rememberCoroutineScope(),
                navController = navController,
                title = "Editar Datos"
            )
        }
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )

        {
            TextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModelU.onValueChange(it, "correo_electronico")
                },
                label = { Text("Correo Electrónico") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = fechaNacimiento,
                onValueChange = {
                    fechaNacimiento = it
                    viewModelU.onValueChange(it, "fecha_nacimiento")
                },
                label = { Text("Fecha de Nacimiento") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    viewModelU.onValueChange(it, "telefono")
                },
                label = { Text("Teléfono") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showConfirmationDialog = true }
            ) {
                Text("Guardar Cambios")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showCancelDialog = true }
            ) {
                Text("Cancelar")
            }
        }
    }
}

