package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberImagePainter
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.viewmodel.UserProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PerfilView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModelU.fetchData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        }
    ) {
        Scaffold(
            topBar = {
                ContenidoSuperior(drawerState, scope, navController)
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()), // Habilitar scroll vertical
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp), // Aquí puedes cambiar el margen superior
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val imagePath = viewModelU.stateHome.image_path ?: ""
                    if (imagePath.isNotEmpty()) {
                        val imagePainter: Painter = rememberImagePainter(
                            data = imagePath,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.app_fondo)
                            }
                        )

                        Image(
                            painter = imagePainter,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(150.dp)
                                .padding(8.dp), // Aquí puedes cambiar el margen alrededor de la imagen
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TopBarUT("Mi Perfil")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    ProfileDetailsView(viewModelU)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Acción para cerrar sesión
                        viewModelU.logout(navController)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Cerrar Sesión")
                }
            }
        }
    }
}

@Composable
fun ProfileDetailsView(profileData: UserProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        ProfileDetailItem(label = "Nombre", value = "${profileData.stateHome.nombre} ${profileData.stateHome.apellido}")
        ProfileDetailItem(label = "Matrícula", value = profileData.stateHome.matricula)
        ProfileDetailItem(label = "Correo Electrónico", value = profileData.stateHome.correo_electronico)
        ProfileDetailItem(label = "Teléfono", value = profileData.stateHome.telefono)
        ProfileDetailItem(label = "Carrera", value = profileData.stateHome.carrera)
        ProfileDetailItem(label = "Rol", value = profileData.stateHome.id_rol)
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Puedes ajustar el padding para separar los elementos
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}