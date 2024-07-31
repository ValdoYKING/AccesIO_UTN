package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.ui.theme.WhiteColor2
import com.pixelfusion.accesio_utn.ui.theme.blackdark
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLight

@Composable
fun DrawerContent3(navController: NavController, currentRoute: String?) {
    Column{
        NavigationRail {
            NavigationRailItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = currentRoute == "home_user_view",
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    navController.navigate("home_user_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.AccountBox, contentDescription = "Credencial") },
                label = { Text("Credencial") },
                selected = currentRoute == "credential_view",
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    navController.navigate("credential_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Mis datos") },
                label = { Text("Mis datos") },
                selected = currentRoute == "profile_view",
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    navController.navigate("profile_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
            NavigationRailItem(
                //icon = { Icon(Icons.Filled.Refresh, contentDescription = "Historial") },
                icon = {
                    val imageHistorialResource = if (isSystemInDarkTheme()) {
                        R.drawable.historial_light
                    } else {
                        R.drawable.historial_black
                    }
                    Image(
                        painter = painterResource(id = imageHistorialResource),
                        contentDescription = "Historial entradas y salidas",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = { Text("Historial") },
                selected = currentRoute == "history_user_view",
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    navController.navigate("history_user_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = "Acerca de") },
                label = { Text("Acerca de") },
                selected = currentRoute == "about_view",
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    navController.navigate("about_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true // Eliminar todas las pantallas anteriores de la pila
                        }
                    }
                }
            )
            //exit
            //Spacer(modifier = Modifier.height())
            Spacer(modifier = Modifier.weight(1f))
            NavigationRailItem(
                /*modifier = Modifier
                    .height(300.dp),*/
                icon = {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión")
                },
                label = { Text("Cerrar Sesión") },
                selected = false,
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = if (isSystemInDarkTheme()) utnGreenLight else utnGreen,
                    selectedTextColor = if (isSystemInDarkTheme()) WhiteColor2 else blackdark,
                    indicatorColor = if (isSystemInDarkTheme()) utnGreen else utnGreenLight
                ),
                onClick = {
                    logout(navController)
                }
            )

        }
    }
}

private lateinit var auth: FirebaseAuth
fun logout(navController: NavController) {
    auth = Firebase.auth
    auth.signOut()
    navController.navigate("login_screen")
}
