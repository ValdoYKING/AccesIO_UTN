package com.pixelfusion.accesio_utn.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.ui.theme.CyanBlue
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.viewmodel.HistoryMyAssistViewModel
import kotlinx.coroutines.delay

@Composable
fun HistoryMyAssistView(navController: NavController, viewModel: HistoryMyAssistViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isLoadingMyHistoryAssist by remember { viewModel._isLoadingMyHistoryAssist }

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
                    TopBarUT("Historial de asistencias")

                    Box(modifier = Modifier.fillMaxSize()) {
                        if (isLoadingMyHistoryAssist) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn {
                                itemsIndexed(viewModel.MyHistoryAssistList) { index, MyAssistPair ->
                                    val (uidMyAccess, MyAssit) = MyAssistPair
                                    MyAssitItem(
                                        number = index + 1,
                                        UidMyAssit = uidMyAccess,
                                        DataAssit = MyAssit,
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    )
    LaunchedEffect(isLoadingMyHistoryAssist) {
        delay(5000)
        if (isLoadingMyHistoryAssist) viewModel._isLoadingMyHistoryAssist.value = false
    }
}

@Composable
private fun MyAssitItem(
    number: Int,
    UidMyAssit: String,
    DataAssit: QrEstudianteAsisteModel,
    navController: NavController,
    viewModel: HistoryMyAssistViewModel
) {
    val qrAsistenciaData = viewModel.qrAsistenciaList.find {
        it.first == DataAssit.uid_qr_asistencia
    }
    Column(
        modifier = Modifier
            .clickable() {
                navController.navigate("my_assist_detail/$UidMyAssit")
            }
    ) {
        ListItem(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite),
            headlineContent = {
                val fechaText = viewModel.convertirFechaATexto(DataAssit.fecha)

                Text(
                    text = "${number}. $fechaText",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            },
            overlineContent = {
                qrAsistenciaData?.let {
                    Text(
                        text = it.second.materia,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            },
            supportingContent = {
                Column {
                    qrAsistenciaData?.let {
                        Text(
                            text = it.second.descripcion_lugar,
                            style = MaterialTheme.typography.bodyMedium,
                            //color = Color.Gray
                        )
                    }
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
                    //.padding(end = 8.dp)
                )
            },
            trailingContent = {
                Column {
                    Text(
                        text = "${DataAssit.hora} hrs",
                        //text = "${DataAssit.fecha} ${DataAssit.hora}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GreenSemiDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ver detalles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CyanBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
        HorizontalDivider()
    }
}