package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.CardTittle
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarTitleNav
import com.pixelfusion.accesio_utn.components.TopBarUTMedium
import com.pixelfusion.accesio_utn.helper.FechaATexto
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.viewmodel.AccessDetailUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.HistoryUserViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun AccessDetailUserView(
    navController: NavController,
    UidAccessUser: String?,
    viewModel: AccessDetailUserViewModel,
    viewModelMyAccess: HistoryUserViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(UidAccessUser) {
        viewModel.fetchData(UidAccessUser)
    }
    val HistoryListUser by viewModel.HistoryListUser.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val userData by viewModel.UserData.collectAsState()

    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFFCE4EC),
                Color(0xFFF8BBD0),
                Color(0xFFF48FB1),
                Color(0xFFF06292),
                Color(0xFFEC407A),
                Color(0xFFE91E63),
                Color(0xFFD81B60),
                Color(0xFFC2185B),
                Color(0xFFAD1457),
                Color(0xFF880E4F),
                Color(0xFF6D0A3B),
                Color(0xFF6D0A3B),
                Color(0xFF880E4F),
                Color(0xFFAD1457),
                Color(0xFFC2185B),
                Color(0xFFD81B60),
                Color(0xFFE91E63),
                Color(0xFFEC407A),
                Color(0xFFF06292),
                Color(0xFFF48FB1),
                Color(0xFFF8BBD0),
                Color(0xFFFCE4EC),
                Color(0xFFFFFFFF)
            )

        )
    }
    val borderWidth = 4.dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    //SuperiorData(drawerState, scope)
                    //ContenidoSuperior(drawerState, scope, navController)
                    ContenidoSuperiorWithTitle(
                        drawerState,
                        scope,
                        navController,
                        "Detalles de acceso"
                    )
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
                        //TopBarTitleNav("Detalles de acceso")
                        userData?.let { data ->
                            OutlinedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSystemInDarkTheme()) Color.White else Color.Black
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        // Renderizar la información del usuario
                                        val imagePath = data.image_path
                                        if (imagePath.isNotEmpty()) {
                                            val imagePainter: Painter = rememberAsyncImagePainter(
                                                ImageRequest.Builder(LocalContext.current)
                                                    .data(data = imagePath)
                                                    .apply(block = fun ImageRequest.Builder.() {
                                                        crossfade(true)
                                                        placeholder(R.drawable.app_fondo)
                                                    }).build()
                                            )
                                            Image(
                                                painter = imagePainter,
                                                contentDescription = "Perfil usuario",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(150.dp)
                                                    .border(
                                                        BorderStroke(
                                                            borderWidth,
                                                            rainbowColorsBrush
                                                        ),
                                                        CircleShape
                                                    )
                                                    .padding(borderWidth)
                                                    .clip(CircleShape)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            /*Text(
                                                text = "NOMBRE:",
                                                fontSize = 16.sp,
                                            )*/
                                            Text(
                                                text = data.nombre + " " + data.apellido,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            /*Text(
                                                text = "Matricula:",
                                                fontSize = 16.sp,
                                            )*/
                                            Text(
                                                text = data.matricula,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = data.carrera,
                                                fontSize = 16.sp,
                                                textAlign = TextAlign.Center,
                                                //fontWeight = FontWeight.Bold,
                                            )
                                            Text(
                                                text = data.id_rol,
                                                fontSize = 16.sp,
                                                //fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        /*HorizontalDivider(
                                            modifier = Modifier.fillMaxWidth(),
                                            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                            thickness = 1.dp
                                        )*/

                                    }
                                }
                            }
                        } ?: run {
                            Text(
                                "No se encontraron detalles del usuario.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        HistoryListUser.firstOrNull()?.let { AccessUser ->
                            val tipoDeRegistro = viewModel.determinarTipoDeAccesoUsuario(
                                AccessUser.fecha_access,
                                AccessUser.hora_access
                            )
                            //viewModelMyAccess.convertirFechaATexto(AccessUser.fecha_access)
                            val fechaText = FechaATexto(AccessUser.fecha_access)
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
                                    text = "Tipo",
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
                                    text = "${AccessUser.fecha_access} ${AccessUser.hora_access} hrs",
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
                            val ubicacion = LatLng(AccessUser.latitude, AccessUser.longitude)
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
                                        .fillMaxWidth()
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
                                        snippet = "$tipoDeRegistro - ${AccessUser.hora_access}"
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