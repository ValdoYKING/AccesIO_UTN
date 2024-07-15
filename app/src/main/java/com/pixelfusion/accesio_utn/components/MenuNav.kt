package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerContent3(navController: NavController, currentRoute: String?) {
    Column {
        NavigationRail {
            //Spacer(modifier = Modifier.width(8.dp))
            NavigationRailItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = currentRoute == "home_user_view",
                onClick = {
                    navController.navigate("home_user_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Credencial") },
                label = { Text("Credencial") },
                selected = currentRoute == "credential_view",
                onClick = {
                    navController.navigate("credential_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.DateRange, contentDescription = "Horarios") },
                label = { Text("Horarios") },
                selected = currentRoute == "horario_view",
                onClick = {
                    navController.navigate("horario_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.AccountBox, contentDescription = "Mis datos") },
                label = { Text("Mis datos") },
                selected = currentRoute == "profile_view",
                onClick = {
                    navController.navigate("profile_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            NavigationRailItem(
                icon = { Icon(Icons.Filled.Info, contentDescription = "Acerca de") },
                label = { Text("Acerca de") },
                selected = currentRoute == "about_view",
                onClick = {
                    navController.navigate("about_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            /*NavigationRailItem(
                icon = { Icon(Icons.Filled.ThumbUp, contentDescription = "Prueba") },
                label = { Text("Siuuuu") },
                selected = currentRoute == "home_user_view",
                onClick = {
                    navController.navigate("home_user_view") {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )*/
            // Puedes agregar más NavigationRailItem aquí para otras vistas
        }
    }
}

