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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.viewmodel.HistoryMyAssistViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyAssistDetailViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun MyAssistDetailView(
    navController: NavController,
    UidMyAssist: String?,
    viewModel: MyAssistDetailViewModel,
    viewModelMyAssist: HistoryMyAssistViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(UidMyAssist) {
        viewModel.fetchData(UidMyAssist)
    }
    val MyAssistList by viewModel.MyAssistList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperior(drawerState, scope, navController)
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        MyAssistList.firstOrNull()?.let { myAssist ->
                            val qrAsistenciaData = viewModelMyAssist.qrAsistenciaList.find {
                                it.first == myAssist.uid_qr_asistencia
                            }
                            val fechaText =
                                viewModelMyAssist.convertirFechaATexto(myAssist.fecha)
                            TopBarUT(fechaText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val imageHistorialResource = if (isSystemInDarkTheme()) {
                                    R.drawable.historial_light
                                } else {
                                    R.drawable.historial_black
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Image(
                                    painter = painterResource(id = imageHistorialResource),
                                    contentDescription = "Historial entradas y salidas",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Materia",
                                    //fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                qrAsistenciaData?.let {
                                    Text(
                                        text = it.second.materia,
                                        fontSize = 20.sp,
                                        //style = MaterialTheme.typography.bodyMedium,
                                        //color = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                qrAsistenciaData?.let {
                                    if (it.second.lugar == "Salon") {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.historial_light
                                        } else {
                                            R.drawable.historial_black
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Historial entradas y salidas",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Salón",
                                            fontSize = 20.sp,
                                            //style = MaterialTheme.typography.bodyMedium,
                                        )
                                    } else if (it.second.lugar == "Laboratorio") {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.historial_light
                                        } else {
                                            R.drawable.historial_black
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Historial entradas y salidas",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Laboratorio",
                                            fontSize = 20.sp,
                                            //style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                qrAsistenciaData?.let {
                                    Text(
                                        text = it.second.descripcion_lugar,
                                        fontSize = 20.sp,
                                        //style = MaterialTheme.typography.bodyMedium,
                                        //color = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val imageHistorialResource = if (isSystemInDarkTheme()) {
                                    R.drawable.historial_light
                                } else {
                                    R.drawable.historial_black
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Image(
                                    painter = painterResource(id = imageHistorialResource),
                                    contentDescription = "Historial entradas y salidas",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Fecha y hora",
                                    //fontWeight = FontWeight.Bold,
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TopBarUT("Ubicacion de asistencia")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val ubicacion = LatLng(myAssist.latitude, myAssist.longitude)
                            val cameraPositionUbicacion = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacion, 30f)
                            }
                            val latitudQRGenerado = qrAsistenciaData?.second?.latitude
                            val longitudQRGenerado = qrAsistenciaData?.second?.longitude
                            val ubicacionQRGenerado =
                                LatLng(latitudQRGenerado ?: 0.0, longitudQRGenerado ?: 0.0)
                            val cameraPositionQR = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacionQRGenerado, 30f)
                            }
                            val horaQr = qrAsistenciaData?.second?.hora
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GoogleMap(
                                    modifier = Modifier
                                        //.fillMaxSize()
                                        .size(400.dp)
                                        .border(
                                            1.dp,
                                            if (isSystemInDarkTheme()) {
                                                Color.White
                                            } else {
                                                Color.Black
                                            }
                                        ),
                                    cameraPositionState = cameraPositionUbicacion
                                ) {
                                    Marker(
                                        state = MarkerState(position = ubicacion),
                                        title = "Mi ubicación",
                                        snippet = "${myAssist.hora} hrs",
                                    )
                                    Marker(
                                        state = MarkerState(position = ubicacionQRGenerado),
                                        title = "QR generado",
                                        snippet = "$horaQr hrs"
                                    )
                                }
                            }

                        } ?: run {
                            Text(
                                "No se encontraron detalles para la asistencia.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }


                }
            }
        }
    )
}