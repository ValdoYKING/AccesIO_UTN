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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.pixelfusion.accesio_utn.model.QrAsistenciaModel
import com.pixelfusion.accesio_utn.ui.theme.CyanBlue
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.viewmodel.AsistenciaListAlumnosViewModel

@Composable
fun AsistenciaListAlumnosView(
    navController: NavController,
    viewModel: AsistenciaListAlumnosViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isLoadingAsistencia by remember { viewModel._isLoadingAsistencia }

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
                        "Lista de QR's de asistencias"
                    )
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
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (isLoadingAsistencia) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn {
                                itemsIndexed(viewModel.AsistenciaUserList) { index, AsistenciaUserPair ->
                                    val (uidAccessUser, AccessUser) = AsistenciaUserPair
                                    ListAccessItemUser(
                                        number = index + 1,
                                        UidQRAccessUser = uidAccessUser,
                                        DataAccess = AccessUser,
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
private fun ListAccessItemUser(
    number: Int,
    UidQRAccessUser: String,
    DataAccess: QrAsistenciaModel,
    navController: NavController,
    viewModel: AsistenciaListAlumnosViewModel
) {
    Column(
        modifier = Modifier
            .clickable() {
                navController.navigate("list_access_users_by_qr/$UidQRAccessUser")
            }
    ) {
        val fechaText = FechaATexto(DataAccess.fecha)
        ListItem(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite),
            headlineContent = {
                Text(
                    text = "${number}. ${DataAccess.titulo}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            },
            overlineContent = {
                Text(
                    text = fechaText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Column {
                    Text(
                        text = DataAccess.lugar,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            leadingContent = {
                val imageListAsistUser = if (isSystemInDarkTheme()) {
                    R.drawable.icon_qr_dark
                } else {
                    R.drawable.icons8_qr_code_100_l
                }
                Image(
                    painter = painterResource(imageListAsistUser),
                    contentDescription = "Generar QR lugar",
                    modifier = Modifier.size(20.dp)
                    //.padding(end = 8.dp)
                )
            },
            trailingContent = {
                Column {
                    Text(
                        text = "${DataAccess.fecha} ${DataAccess.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenSemiDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ver asistentes",
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