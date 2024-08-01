package com.pixelfusion.accesio_utn.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.helper.FechaATexto
import com.pixelfusion.accesio_utn.model.QrEstudianteAsisteModel
import com.pixelfusion.accesio_utn.ui.theme.CyanBlue
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.viewmodel.ListAssistUsersQrViewModel

@Composable
fun ListAssistUsersQrView(
    navController: NavController,
    UidQRAccessUser: String,
    viewModel: ListAssistUsersQrViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(UidQRAccessUser) {
        viewModel.fetchData(UidQRAccessUser)
    }

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
                        "Alumnos que asistieron"
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.Center
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Column {
                            LazyColumn {
                                itemsIndexed(viewModel.AsistenciaUserList) { index, studentAssistPair ->
                                    val (uidQrAsistencia, studentAssist) = studentAssistPair
                                    ListStudentAssistItem(
                                        number = index + 1,
                                        uidQrAsistencia = uidQrAsistencia,
                                        studentAssist = studentAssist,
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
}


@Composable
private fun ListStudentAssistItem(
    number: Int,
    uidQrAsistencia: String,
    studentAssist: QrEstudianteAsisteModel,
    navController: NavController,
    viewModel: ListAssistUsersQrViewModel
) {
    val userDetails = viewModel.UserDetailsMap[studentAssist.uid_user]
    val userName = userDetails?.let { "${it.nombre} ${it.apellido}" } ?: "Desconocido"
    val matricula = userDetails?.matricula ?: "Desconocido"
    val fechaText = FechaATexto(studentAssist.fecha)
    Column(
        modifier = Modifier
            .clickable {
                navController.navigate("detail_student_assist/$uidQrAsistencia")
            }
    ) {
        ListItem(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite),
            headlineContent = {
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                )
            },
            overlineContent = {
                Text(
                    text = matricula,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Column {
                    Text(
                        text = "$fechaText a las ${studentAssist.hora} hrs",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            leadingContent = {
                Image(
                    painter = painterResource(if (isSystemInDarkTheme()) R.drawable.icons8_qr_code_100_l else R.drawable.icon_qr_dark),
                    contentDescription = "Generar QR lugar",
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingContent = {
                Text(
                    text = "Ver detalles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CyanBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        )
        HorizontalDivider()
    }
}