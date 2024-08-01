package com.pixelfusion.accesio_utn.view

import ContenidoSuperiorWithTitle
import UserProfileViewModel
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pixelfusion.accesio_utn.components.DrawerContent3


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditarDatosView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showConfirmationDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, "editar_datos_view")
        }
    ) {
        Scaffold(
            topBar = {
                ContenidoSuperiorWithTitle(
                    drawerState = drawerState,
                    scope = scope,
                    navController = navController,
                    title = "Editar Datos"
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campos de entrada para editar los datos del usuario
                OutlinedTextField(
                    value = viewModelU.stateHome.nombre,
                    onValueChange = { viewModelU.onValueChange(it, "nombre") },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.apellido,
                    onValueChange = { viewModelU.onValueChange(it, "apellido") },
                    label = { Text("Apellido") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.correo_electronico,
                    onValueChange = { viewModelU.onValueChange(it, "correo_electronico") },
                    label = { Text("Correo Electrónico") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.telefono,
                    onValueChange = { viewModelU.onValueChange(it, "telefono") },
                    label = { Text("Teléfono") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.carrera,
                    onValueChange = { viewModelU.onValueChange(it, "carrera") },
                    label = { Text("Carrera") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.fecha_nacimiento,
                    onValueChange = { viewModelU.onValueChange(it, "fecha_nacimiento") },
                    label = { Text("Fecha de Nacimiento") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.num_seguro_social,
                    onValueChange = { viewModelU.onValueChange(it, "num_seguro_social") },
                    label = { Text("Número de Seguro Social") }
                )
                OutlinedTextField(
                    value = viewModelU.stateHome.matricula,
                    onValueChange = { viewModelU.onValueChange(it, "matricula") },
                    label = { Text("Matrícula") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showConfirmationDialog = true
                    }
                ) {
                    Text("Guardar Cambios")
                }

                if (showConfirmationDialog) {
                    AlertDialog(
                        onDismissRequest = { showConfirmationDialog = false },
                        title = { Text("Confirmar") },
                        text = { Text("¿Deseas guardar los cambios?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showConfirmationDialog = false
                                    val updatedData = mapOf(
                                        "nombre" to viewModelU.stateHome.nombre.takeIf { it.isNotEmpty() },
                                        "apellido" to viewModelU.stateHome.apellido.takeIf { it.isNotEmpty() },
                                        "correo_electronico" to viewModelU.stateHome.correo_electronico.takeIf { it.isNotEmpty() },
                                        "telefono" to viewModelU.stateHome.telefono.takeIf { it.isNotEmpty() },
                                        "carrera" to viewModelU.stateHome.carrera.takeIf { it.isNotEmpty() },
                                        "fecha_nacimiento" to viewModelU.stateHome.fecha_nacimiento.takeIf { it.isNotEmpty() },
                                        "num_seguro_social" to viewModelU.stateHome.num_seguro_social.takeIf { it.isNotEmpty() },
                                        "matricula" to viewModelU.stateHome.matricula.takeIf { it.isNotEmpty() }
                                    ).filterValues { it != null } as Map<String, String>
                                    viewModelU.updateUserData(navController, updatedData)
                                    Toast.makeText(context, "Datos actualizados", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Text("Aceptar")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showConfirmationDialog = false }
                            ) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
