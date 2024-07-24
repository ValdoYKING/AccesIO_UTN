package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.SuperiorData
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrLugarModel
import com.pixelfusion.accesio_utn.ui.theme.BlueMarine
import com.pixelfusion.accesio_utn.ui.theme.CyanBlue
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.ui.theme.OrangeWarning
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.viewmodel.ListQrGenerateViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListQrGenerateView(navController: NavController, viewModel: ListQrGenerateViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Asistencia", "Lugar")

    val isLoadingAsistencia by remember { viewModel._isLoadingAsistencia }
    val isLoadingLugar by remember { viewModel._isLoadingLugar }

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
                    verticalArrangement = Arrangement.Top
                ) {
                    TopBarUT("Mis QR's")

                    // TabRow for switching between lists
                    TabRow(selectedTabIndex = selectedTabIndex) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Conditional display of QR items based on the selected tab
                    if (selectedTabIndex == 0) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (isLoadingAsistencia) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                LazyColumn {
                                    itemsIndexed(viewModel.qrAsistenciaList) { index, qrAsistenciaPair ->
                                        val (uidAsistencia, qrAsistencia) = qrAsistenciaPair
                                        QrAsistenciaItem(
                                            number = index + 1,
                                            qrUidAsistencia = uidAsistencia,
                                            qrAsistencia = qrAsistencia,
                                            navController = navController,
                                            viewModel = viewModel
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (isLoadingLugar) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            } else {
                                LazyColumn {
                                    itemsIndexed(viewModel.qrLugarList) { index, qrLugarPair ->
                                        val (uid, qrLugar) = qrLugarPair
                                        QrLugarItem(
                                            number = index + 1,
                                            qrUidLugar = uid,
                                            qrLugar = qrLugar,
                                            navController = navController
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    )

    LaunchedEffect(isLoadingAsistencia, isLoadingLugar) {
        delay(5000)
        if (isLoadingAsistencia) viewModel._isLoadingAsistencia.value = false
        if (isLoadingLugar) viewModel._isLoadingLugar.value = false
    }
}


@Composable
fun QrAsistenciaItem(
    number: Int,
    qrUidAsistencia: String,
    qrAsistencia: QrAsistenciaModel,
    navController: NavController,
    viewModel: ListQrGenerateViewModel
) {
    val fecha = qrAsistencia.fecha
    val hora = qrAsistencia.hora
    val duracion = qrAsistencia.duracion
    val nuevaFechaHora = viewModel.calcularNuevaFechaYHora(fecha, hora, duracion)

    val currentDateTime = LocalDateTime.now()
    val isExpired = nuevaFechaHora.isBefore(currentDateTime)

    val backgroundColor = if (isExpired) Color.Gray else MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .clickable(enabled = !isExpired) {
                navController.navigate("qr_asistencia_detail/$qrUidAsistencia")
            }
        //.background(backgroundColor)
        //.padding(16.dp)
    ) {
        ListItem(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite),
            headlineContent = {
                Text(
                    text = "${number}. ${qrAsistencia.titulo}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    //color = if (isExpired) Color.DarkGray else MaterialTheme.colorScheme.onSurface
                )
            },
            overlineContent = {
                Text(
                    text = qrAsistencia.materia,
                    style = MaterialTheme.typography.labelLarge,
                    //color = if (isExpired) Color.DarkGray else MaterialTheme.colorScheme.onSurface
                )
            },
            supportingContent = {
                Column {
                    Text(
                        text = qrAsistencia.lugar,
                        style = MaterialTheme.typography.bodyMedium,
                        //color = if (isExpired) Color.DarkGray else MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            leadingContent = {
                Spacer(modifier = Modifier.height(4.dp))
                val imageGenerateQrResource = if (isSystemInDarkTheme()) {
                    R.drawable.icons8_qr_code_100_l
                } else {
                    R.drawable.icon_qr_dark
                }
                Image(
                    painter = painterResource(id = imageGenerateQrResource),
                    contentDescription = "Generar QR lugar",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingContent = {
                Column {
                    Text(
                        text = "${qrAsistencia.fecha} ${qrAsistencia.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenSemiDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = nuevaFechaHora.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                        style = MaterialTheme.typography.bodySmall,
                        color = OrangeWarning
                    )
                    if (isExpired) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "EXPIRADO",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ver QR",
                            style = MaterialTheme.typography.bodyMedium,
                            color = CyanBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )
        HorizontalDivider()
    }
}


@Composable
fun QrLugarItem(
    number: Int,
    qrUidLugar: String,
    qrLugar: QrLugarModel,
    navController: NavController
) {
    Column(
        modifier = Modifier.clickable {
            navController.navigate("qr_lugar_detail/$qrUidLugar")
        }
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = "${number}. ${qrLugar.titulo}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            supportingContent = {
                Text(text = "Lugar: ${qrLugar.lugar}", style = MaterialTheme.typography.bodyMedium)
            },
            trailingContent = {
                Column {
                    Text(
                        text = "${qrLugar.fecha} ${qrLugar.hora}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ver QR",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyanBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            leadingContent = {
                val imageGenerateQrResource = if (isSystemInDarkTheme()) {
                    R.drawable.icons8_qr_code_100_l
                } else {
                    R.drawable.icon_qr_dark
                }
                Image(
                    painter = painterResource(id = imageGenerateQrResource),
                    contentDescription = "Generar QR lugar",
                    modifier = Modifier.size(20.dp)
                )
            }
        )
        HorizontalDivider(color = BlueMarine)
    }
}
