package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUTMedium
import com.pixelfusion.accesio_utn.viewmodel.DetailStudentAssistViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailStudentAssistView(
    navController: NavController,
    uidStudentAsistencia: String,
    uidUserStudent: String,
    viewModel: DetailStudentAssistViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Fetch data when uidStudentAsistencia or uidUserStudent changes
    LaunchedEffect(uidStudentAsistencia, uidUserStudent) {
        viewModel.fetchData(uidStudentAsistencia)
        viewModel.fetchUserDetails(uidUserStudent)
    }

    val myAssistList by viewModel.MyAssistList.collectAsState()
    val userDetailsMap by viewModel.UserDetailsMap.collectAsState()
    val qrAsistenciaMap by viewModel.QrAsistenciaMap.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Get QR Asistencia data and User details
    val myAssist = myAssistList.firstOrNull()
    val qrAsistenciaData = myAssist?.uid_qr_asistencia?.let { qrAsistenciaMap[it] }
    val userDetails = userDetailsMap[uidUserStudent]

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperiorWithTitle(
                        drawerState,
                        scope,
                        navController,
                        "Detalles del alumno"
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator()
                        }

                        myAssist == null || qrAsistenciaData == null -> {
                            Text(
                                "No se encontraron detalles para la asistencia",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        else -> {
                            // Mostrar los datos de asistencia y usuario
                            // Nombre del usuario
                            userDetails?.let { user ->
                                Text(
                                    text = "Nombre del usuario: ${user.nombre} ${user.apellido}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } ?: run {
                                Text(
                                    text = "No se encontró información del usuario.",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Fecha y Hora
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.historial_black),
                                    contentDescription = "Fecha y hora",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Fecha y hora",
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "${myAssist.fecha} ${myAssist.hora} hrs",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Materia
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val imageHistorialResource = if (isSystemInDarkTheme()) {
                                    R.drawable.historial_light
                                } else {
                                    R.drawable.historial_black
                                }
                                Image(
                                    painter = painterResource(id = imageHistorialResource),
                                    contentDescription = "Materia",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = qrAsistenciaData.materia,
                                    fontSize = 20.sp
                                )
                            }

                            // Lugar
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val lugarText = when (qrAsistenciaData.lugar) {
                                    "Salon" -> "Salón"
                                    "Laboratorio" -> "Laboratorio"
                                    else -> "Desconocido"
                                }
                                val lugarImage = if (isSystemInDarkTheme()) {
                                    R.drawable.historial_light
                                } else {
                                    R.drawable.historial_black
                                }
                                Image(
                                    painter = painterResource(id = lugarImage),
                                    contentDescription = lugarText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = lugarText,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = qrAsistenciaData.descripcion_lugar,
                                    fontSize = 20.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Ubicación
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TopBarUTMedium("Ubicación de asistencia")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val ubicacion = LatLng(myAssist.latitude, myAssist.longitude)
                            val cameraPositionUbicacion = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacion, 20f)
                            }
                            val latitudQRGenerado = qrAsistenciaData.latitude
                            val longitudQRGenerado = qrAsistenciaData.longitude
                            val ubicacionQRGenerado = LatLng(latitudQRGenerado, longitudQRGenerado)

                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .size(400.dp)
                                    .border(
                                        1.dp,
                                        if (isSystemInDarkTheme()) Color.White else Color.Black
                                    ),
                                cameraPositionState = cameraPositionUbicacion
                            ) {
                                Marker(
                                    state = MarkerState(position = ubicacion),
                                    title = "Mi ubicación",
                                    snippet = "${myAssist.hora} hrs"
                                )
                                Marker(
                                    state = MarkerState(position = ubicacionQRGenerado),
                                    title = "QR generado",
                                    snippet = "${myAssist.hora} hrs"
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
