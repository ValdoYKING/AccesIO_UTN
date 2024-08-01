package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
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
import com.pixelfusion.accesio_utn.components.FullScreenQRCodeDialog
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.helper.generateQRCodeAssist
import com.pixelfusion.accesio_utn.helper.printQrCode
import com.pixelfusion.accesio_utn.viewmodel.QrAsistenciaDetailViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun QrAsistenciaDetailView(
    navController: NavController,
    qrUidAsistencia: String?,
    viewModel: QrAsistenciaDetailViewModel
) {
    LaunchedEffect(qrUidAsistencia) {
        viewModel.fetchData(qrUidAsistencia)
    }

    val qrAsistenciaList by viewModel.qrAsistenciaList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ModalNavigationDrawer(
        drawerState = rememberDrawerState(DrawerValue.Closed),
        drawerContent = {
            DrawerContent3(navController, currentRoute = null)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperiorWithTitle(
                        drawerState = rememberDrawerState(DrawerValue.Closed),
                        scope = rememberCoroutineScope(),
                        navController = navController,
                        title = "QR Asistencia"
                    )
                },
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        qrAsistenciaList.firstOrNull()?.let { asistencia ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                //horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TopBarUT(asistencia.titulo)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.icono_arena_white
                                        } else {
                                            R.drawable.icono_arena_dark
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Historial entradas y salidas",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Duración del codigo QR:",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = "${asistencia.duracion} horas",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.icon_school_white
                                        } else {
                                            R.drawable.icon_school_dark
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Historial entradas y salidas",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "División",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = asistencia.division,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.icono_reloj_white
                                        } else {
                                            R.drawable.icono_reloj_dark
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Cuatrimestre",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Cuatrimestre",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = asistencia.cuatrimestre,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.icono_materia_white
                                        } else {
                                            R.drawable.icono_materia_dark
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
                                        Text(
                                            text = asistencia.materia,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val imageHistorialResource = if (isSystemInDarkTheme()) {
                                            R.drawable.icono_place_white
                                        } else {
                                            R.drawable.icono_place_dark
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Image(
                                            painter = painterResource(id = imageHistorialResource),
                                            contentDescription = "Historial entradas y salidas",
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Lugar",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = asistencia.lugar,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
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
                                            text = "Fecha y hora generado",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = "${asistencia.fecha} ${asistencia.hora} hrs",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    //QRCodeAsistencia(qrUidAsistencia ?: "")
                                    QRCodeAsistencia(
                                        qrUidAsistencia ?: "",
                                        300,
                                        300,
                                        context = LocalContext.current,
                                        asistencia.titulo
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TopBarUT("Ubicacion de QR generado")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val ubicacion = LatLng(asistencia.latitude, asistencia.longitude)
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
                                            title = "Ubicación de QR",
                                            snippet = "QR ${asistencia.titulo} generado"
                                        )
                                    }
                                }
                            }
                        } ?: run {
                            Text(
                                "No se encontraron detalles para este QR.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun QRCodeAsistencia(
    UidQrAsistencia: String,
    qrWidth: Int = 300,
    qrHeight: Int = 300,
    context: Context,
    titulo: String
) {
    var showFullscreenQr by remember { mutableStateOf(false) }
    val qrCodeBitmap = generateQRCodeAssist(UidQrAsistencia, qrWidth, qrHeight)

    qrCodeBitmap?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier
                .clickable { showFullscreenQr = true }
                .size(300.dp)) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {

                Button(onClick = { printQrCode(context, it, titulo) }) {
                    Text(text = "Imprimir QR")
                }
                /*Comentado por errores :c*/
                /*Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {createPdfFromQrCode(it, "qrLugar $titulo")}) {
                    Text(text = "Generate PDF")
                }*/
            }
        }
        if (showFullscreenQr) {
            FullScreenQRCodeDialog(qrCodeBitmap = it, onDismiss = { showFullscreenQr = false })
        }
    }
}