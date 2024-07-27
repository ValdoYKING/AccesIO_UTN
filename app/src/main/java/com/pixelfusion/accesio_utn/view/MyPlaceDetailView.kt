package com.pixelfusion.accesio_utn.view

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
import androidx.compose.material3.Button
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
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.components.TopBarUTMedium
import com.pixelfusion.accesio_utn.viewmodel.HistoryPlaceViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyPlaceDetailViewModel

@Composable
fun MyPlaceDetailView(
    navController: NavController,
    UidMyPlace: String?,
    viewModel: MyPlaceDetailViewModel,
    viewModelHistoryPlace: HistoryPlaceViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(UidMyPlace) {
        viewModel.fetchData(UidMyPlace)
    }
    val MyPlaceList by viewModel.MyPlaceList.collectAsState()
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
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        MyPlaceList.firstOrNull()?.let { myPlace ->
                            val qrPlaceData = viewModelHistoryPlace.qrLugarList.find {
                                it.first == myPlace.uid_qr_lugar
                            }
                            val fechaText =
                                viewModelHistoryPlace.convertirFechaATexto(myPlace.fecha)
                            TopBarUTMedium(fechaText)
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
                                qrPlaceData?.let {
                                    Text(
                                        text = it.second.tipo,
                                        fontSize = 20.sp,
                                        //style = MaterialTheme.typography.bodyMedium,
                                        //color = Color.Gray
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                qrPlaceData?.let {
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
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                qrPlaceData?.let {
                                    Text(
                                        text = it.second.lugar,
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
                                    text = "${myPlace.fecha} ${myPlace.hora} hrs",
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
                                TopBarUTMedium("Ubicacion de asistencia")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val ubicacion = LatLng(myPlace.latitude, myPlace.longitude)
                            val cameraPositionUbicacion = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacion, 30f)
                            }
                            val latitudQRGenerado = qrPlaceData?.second?.latitude
                            val longitudQRGenerado = qrPlaceData?.second?.longitude
                            val ubicacionQRGenerado =
                                LatLng(latitudQRGenerado ?: 0.0, longitudQRGenerado ?: 0.0)
                            val cameraPositionQR = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacionQRGenerado, 30f)
                            }
                            val horaQr = qrPlaceData?.second?.hora
                            val fechaQr = qrPlaceData?.second?.fecha
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
                                        snippet = "${myPlace.hora} hrs",
                                    )
                                    Marker(
                                        state = MarkerState(position = ubicacionQRGenerado),
                                        title = "QR generado",
                                        snippet = fechaQr
                                    )
                                }
                            }

                        } ?: run {
                            Text(
                                "No se encontraron detalles para el lugar.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    )

}