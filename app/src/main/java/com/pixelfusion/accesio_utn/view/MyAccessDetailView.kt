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
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUTMedium
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.viewmodel.HistoryUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.MyAccessDetailViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun MyAccessDetailView(
    navController: NavController,
    UidMyAccess: String?,
    viewModel: MyAccessDetailViewModel,
    viewModelMyAccess: HistoryUserViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(UidMyAccess) {
        viewModel.fetchData(UidMyAccess)
    }
    val MyHistoryList by viewModel.MyHistoryList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    //SuperiorData(drawerState, scope)
                    ContenidoSuperiorWithTitle(drawerState, scope, navController, "Detalles de acceso ")
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
                    Spacer(modifier = Modifier.height(16.dp))
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        MyHistoryList.firstOrNull()?.let { myAccess ->
                            val tipoDeRegistro = viewModelMyAccess.determinarTipoDeRegistro(
                                myAccess.fecha_access,
                                myAccess.hora_access
                            )
                            val fechaText =
                                viewModelMyAccess.convertirFechaATexto(myAccess.fecha_access)
                            TopBarUTMedium(fechaText)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val imageHistorialResource = if (isSystemInDarkTheme()) {
                                    R.drawable.iconodoor_white
                                } else {
                                    R.drawable.icono_door_dark
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Image(
                                    painter = painterResource(id = imageHistorialResource),
                                    contentDescription = "Historial entradas y salidas",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tipo",
                                    //fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = tipoDeRegistro,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = if (tipoDeRegistro == "Entrada") GreenSemiDark else Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val imageHistorialResource = if (isSystemInDarkTheme()) {
                                    R.drawable.icono_horafecha_white
                                } else {
                                    R.drawable.icono_horafecha_dark
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
                                    text = "${myAccess.fecha_access} ${myAccess.hora_access} hrs",
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
                                TopBarUTMedium("Ubicacion de acceso")
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val ubicacion = LatLng(myAccess.latitude, myAccess.longitude)
                            val cameraPositionUbicacion = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(ubicacion, 20f)
                            }
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
                                        title = "Ubicación",
                                        snippet = "$tipoDeRegistro - ${myAccess.hora_access}"
                                    )
                                }
                            }

                        } ?: run {
                            Text(
                                "No se encontraron detalles para este acceso.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }


                }
            }
        }
    )
}