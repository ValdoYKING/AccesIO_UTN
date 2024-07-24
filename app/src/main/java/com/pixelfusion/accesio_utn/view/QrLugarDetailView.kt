package com.pixelfusion.accesio_utn.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.FullScreenQRCodeDialog
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.helper.createPdfFromQrCode
import com.pixelfusion.accesio_utn.helper.generateQRCodePlace
import com.pixelfusion.accesio_utn.helper.printQrCode
import com.pixelfusion.accesio_utn.viewmodel.QrLugarDetailViewModel

@Composable
fun QrLugarDetailView(
    navController: NavController,
    qrUidLugar: String?,
    viewModel: QrLugarDetailViewModel,
) {
    LaunchedEffect(qrUidLugar) {
        viewModel.fetchData(qrUidLugar)
    }

    val qrLugarList by viewModel.qrLugarList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    ModalNavigationDrawer(
        drawerState = rememberDrawerState(DrawerValue.Closed),
        drawerContent = {
            DrawerContent3(navController, currentRoute = null)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperior(
                        drawerState = rememberDrawerState(DrawerValue.Closed),
                        scope = rememberCoroutineScope(),
                        navController = navController
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
                        qrLugarList.firstOrNull()?.let { lugar ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TopBarUT(lugar.titulo)
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {

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
                                            text = "Tipo de lugar:",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = lugar.tipo,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
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
                                            text = "Lugar",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = lugar.lugar,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
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
                                            text = "Fecha y hora generado",
                                            //fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = "${lugar.fecha} ${lugar.hora} hrs",
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
                                    QRCodeLugar(
                                        qrUidLugar ?: "",
                                        300,
                                        300,
                                        context = LocalContext.current,
                                        lugar.titulo
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                //map
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TopBarUT("Ubicacion de QR generado")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val ubicacion = LatLng(lugar.latitude, lugar.longitude)
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
                                            snippet = "QR ${lugar.titulo} generado"
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
fun QRCodeLugar(
    UidQrLugar: String, qrWidth: Int = 300, qrHeight: Int = 300, context: Context, titulo: String
) {
    var showFullscreenQr by remember { mutableStateOf(false) }
    val qrCodeBitmap = generateQRCodePlace(UidQrLugar, qrWidth, qrHeight)

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


