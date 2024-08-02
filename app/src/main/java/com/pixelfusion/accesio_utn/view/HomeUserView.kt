package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.CredencialMenuItem
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.EscanearQRLugarMenuItem
import com.pixelfusion.accesio_utn.components.EscanearQRMenuItem

import com.pixelfusion.accesio_utn.components.GenerarQRMenuItem
import com.pixelfusion.accesio_utn.components.HistorialLugar
import com.pixelfusion.accesio_utn.components.HistorialMenuItem
import com.pixelfusion.accesio_utn.components.HorarioMenuItem

import com.pixelfusion.accesio_utn.components.ListaAccesosUsers
import com.pixelfusion.accesio_utn.components.ListaAsistenciaAlumnos
import com.pixelfusion.accesio_utn.components.ListaQRMenuItem
import com.pixelfusion.accesio_utn.components.MiAsistenciaMenuItem
import com.pixelfusion.accesio_utn.components.MiHistorialAsistencia
import com.pixelfusion.accesio_utn.model.ButtonData
import com.pixelfusion.accesio_utn.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeUserView(
    navController: NavController,
    viewModelUser: HomeViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val dataH = viewModelUser.stateHome
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var showDialog by remember { mutableStateOf(false) }
    val buttonData = remember { mutableStateListOf<ButtonData>() }
    LaunchedEffect(Unit) {
        viewModelUser.fetchData()
    }
    // BackHandler para interceptar el botón de retroceso
    BackHandler(enabled = true) {
        showDialog = true
    }

    LaunchedEffect(dataH.id_rol) {
        buttonData.clear()
        if (buttonData.isEmpty()) {
            when (dataH.id_rol) {
                "DEVELOPER" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData("horario_view", "HorarioMenuItem", navController),
                            ButtonData(
                                "scan_qr_assist_view",
                                "MiAsistenciaMenuItem",
                                navController
                            ),
                            ButtonData("scan_qr_access_view", "EscanearQRMenuItem", navController),
                            ButtonData("generate_qr_view", "GenerarQRMenuItem", navController),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                            ButtonData("lista_mi_qr", "ListaQRMenuItem", navController),
                            ButtonData(
                                "scan_qr_lugar_view",
                                "EscanearQRLugarMenuItem",
                                navController
                            ),
                            ButtonData(
                                "history_my_assist_view",
                                "MiHistorialAsistencia",
                                navController
                            ),
                            ButtonData(
                                "asistencia_list_alumnos_view",
                                "ListaAsistenciaAlumnos",
                                navController
                            ),
                            ButtonData(
                                "historial_qr_lugar_view",
                                "HistorialLugares",
                                navController
                            ),
                            ButtonData(
                                "accesos_list_users_view",
                                "ListaAccesosUsuarios",
                                navController
                            )

                        )
                    )
                }
                "ADMINISTRATIVO" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData("horario_view", "HorarioMenuItem", navController),
                            ButtonData("scan_qr_access_view", "EscanearQRMenuItem", navController),
                            ButtonData("generate_qr_view", "GenerarQRMenuItem", navController),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                            ButtonData("lista_mi_qr", "ListaQRMenuItem", navController),
                            ButtonData(
                                "scan_qr_lugar_view",
                                "EscanearQRLugarMenuItem",
                                navController
                            ),
                            ButtonData(
                                "historial_qr_lugar_view",
                                "HistorialLugares",
                                navController
                            ),
                            ButtonData(
                                "accesos_list_users_view",
                                "ListaAccesosUsuarios",
                                navController
                            )

                        )
                    )
                }

                "DOCENTE" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData(
                                "horario_profesor_view",
                                "HorarioProfesorMenuItem",
                                navController
                            ),
                            ButtonData("generate_qr_view", "GenerarQRMenuItem", navController),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                            ButtonData("lista_mi_qr", "ListaQRMenuItem", navController),
                            ButtonData(
                                "scan_qr_lugar_view",
                                "EscanearQRLugarMenuItem",
                                navController
                            ),
                            ButtonData(
                                "asistencia_list_alumnos_view",
                                "ListaAsistenciaAlumnos",
                                navController
                            ),
                            ButtonData(
                                "historial_qr_lugar_view",
                                "HistorialLugares",
                                navController
                            ),
                        )
                    )
                }

                "VISITA" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                        )
                    )
                }

                "PERSONAL" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData("scan_qr_access_view", "EscanearQRMenuItem", navController),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                            ButtonData(
                                "scan_qr_lugar_view",
                                "EscanearQRLugarMenuItem",
                                navController
                            ),
                            ButtonData(
                                "historial_qr_lugar_view",
                                "HistorialLugares",
                                navController
                            ),
                        )
                    )
                }

                "ESTUDIANTE" -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                            ButtonData("horario_view", "HorarioMenuItem", navController),
                            ButtonData(
                                "scan_qr_assist_view",
                                "MiAsistenciaMenuItem",
                                navController
                            ),
                            ButtonData("history_user_view", "HistorialMenuItem", navController),
                            ButtonData(
                                "scan_qr_lugar_view", "EscanearQRLugarMenuItem",
                                navController
                            ),
                            ButtonData(
                                "history_my_assist_view",
                                "MiHistorialAsistencia",
                                navController
                            ),
                            ButtonData(
                                "historial_qr_lugar_view",
                                "HistorialLugares",
                                navController
                            ),
                        )
                    )
                }

                else -> {
                    buttonData.addAll(
                        listOf(
                            ButtonData("credential_view", "CredencialMenuItem", navController),
                        )
                    )
                }
            }
        }

    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.utnfoto),
                                contentDescription = "Logo UTN",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(4.dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            //viewModelUser.salirApp()
                            showDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Logout"
                            )
                        }
                    },
                )
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Salir de AccesIO UTN") },
                        text = { Text("¿Estás seguro de que quieres salir de la aplicación?") },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModelUser.salirApp()
                                showDialog = false
                            }) {
                                Text("Sí")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (viewModelUser.isLoading) {
                        Spacer(modifier = Modifier.height(20.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(alignment = Alignment.CenterHorizontally)
                        )
                    } else {
                        /*Text(
                            text = "Mi cuenta",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))*/
                        Text(
                            text = dataH.nombre + " " + dataH.apellido,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dataH.id_rol,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Matricula",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = dataH.matricula,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
                if (viewModelUser.isLoading) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(buttonData.chunked(2)) { rowButtons ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                rowButtons.forEach { button ->
                                    Button(
                                        onClick = { button.onClick() },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        when (button.text) {
                                            "CredencialMenuItem" -> CredencialMenuItem()
                                            "HorarioMenuItem" -> HorarioMenuItem()
                                            "MiAsistenciaMenuItem" -> MiAsistenciaMenuItem()
                                            "EscanearQRMenuItem" -> EscanearQRMenuItem()
                                            "GenerarQRMenuItem" -> GenerarQRMenuItem()
                                            "HistorialMenuItem" -> HistorialMenuItem()
                                            "ListaQRMenuItem" -> ListaQRMenuItem()
                                            "EscanearQRLugarMenuItem" -> EscanearQRLugarMenuItem()
                                            "MiHistorialAsistencia" -> MiHistorialAsistencia()
                                            "ListaAsistenciaAlumnos" -> ListaAsistenciaAlumnos()
                                            "HistorialLugares" -> HistorialLugar()
                                            "ListaAccesosUsuarios" -> ListaAccesosUsers()


                                            else -> Text(text = button.text)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}