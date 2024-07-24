package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import com.pixelfusion.accesio_utn.viewmodel.ListQrGenerateViewModel
import kotlinx.coroutines.delay

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
                                            navController = navController
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
    navController: NavController
) {
    Column(
        modifier = Modifier.clickable {
            navController.navigate("qr_asistencia_detail/$qrUidAsistencia")
        }
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = "${number}. ${qrAsistencia.titulo}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            overlineContent = {
                Text(
                    text = qrAsistencia.materia,
                    //text = "Materia: ${qrAsistencia.materia}",
                    style = MaterialTheme.typography.labelLarge
                )
            },
            supportingContent = {
                Text(
                    text = qrAsistencia.lugar,
                    //text = "Lugar: ${qrAsistencia.lugar}",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            leadingContent = {
                Spacer(modifier = Modifier.height(4.dp))
                val imageGenerateQrResource = if (isSystemInDarkTheme()) {
                    //R.drawable.icon_qr_dark
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
                        color = Color.Green
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${qrAsistencia.fecha} ${qrAsistencia.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )
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
                Text(
                    text = "${qrLugar.fecha} ${qrLugar.hora}",
                    style = MaterialTheme.typography.bodySmall
                )
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
                    modifier = Modifier.size(18.dp)
                )
            }
        )
        HorizontalDivider(color = BlueMarine)
    }
}
