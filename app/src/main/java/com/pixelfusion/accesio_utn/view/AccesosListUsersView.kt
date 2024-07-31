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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.model.AccessUserModel
import com.pixelfusion.accesio_utn.ui.theme.CyanBlue
import com.pixelfusion.accesio_utn.ui.theme.GreenSemiDark
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.viewmodel.AccesosListUsersViewModel
import kotlinx.coroutines.delay

/*Lista de accesos de usuarios*/
@Composable
fun AccesosListUsersView(navController: NavController, viewModel: AccesosListUsersViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val isLoadingMyHistory by remember { viewModel._isLoadingMyHistory }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    //ContenidoSuperior(drawerState, scope, navController)
                    ContenidoSuperiorWithTitle(drawerState, scope, navController, "Accesos UTN")
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
                        if (isLoadingMyHistory) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn {
                                itemsIndexed(viewModel.MyHistoryList) { index, MyAccessPair ->
                                    val (uidMyAccess, MyAccess) = MyAccessPair
                                    ListAccessItem(
                                        number = index + 1,
                                        UidMyAccess = uidMyAccess,
                                        DataAccess = MyAccess,
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
    LaunchedEffect(isLoadingMyHistory) {
        delay(5000)
        if (isLoadingMyHistory) viewModel._isLoadingMyHistory.value = false
    }
}

@Composable
private fun ListAccessItem(
    number: Int,
    UidMyAccess: String,
    DataAccess: AccessUserModel,
    navController: NavController,
    viewModel: AccesosListUsersViewModel
) {
    val tipoDeRegistro =
        viewModel.determinarTipoDeRegistro(DataAccess.fecha_access, DataAccess.hora_access)
    Column(
        modifier = Modifier
            .clickable() {
                navController.navigate("details_access_user/$UidMyAccess")
            }
    ) {
        ListItem(
            modifier = Modifier
                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite),
            headlineContent = {
                val fechaText = viewModel.convertirFechaATexto(DataAccess.fecha_access)
                Text(
                    text = "${number}. $fechaText",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            },
            overlineContent = {
                Text(
                    text = DataAccess.matricula,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                )
            },
            supportingContent = {
                Column {
                    Text(
                        text = "${DataAccess.hora_access} hrs",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            leadingContent = {
                //Spacer(modifier = Modifier.height(14.dp))
                val imageGenerateQrResource =
                    if (tipoDeRegistro == "Entrada") R.drawable.login_icon else R.drawable.exit_icon
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
                        text = "${DataAccess.fecha_access} ${DataAccess.hora_access}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenSemiDark
                    )
                    /*AÑADIIR TIPO DE REGISTRO AQUI*/
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tipoDeRegistro,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (tipoDeRegistro == "Entrada") GreenSemiDark else Color.Red
                    )
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