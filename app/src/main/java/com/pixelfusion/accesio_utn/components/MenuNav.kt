package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DrawerContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // User profile information (optional)
        // ...

        // Drawer items
        DrawerItem(
            label = "Mi credencial",
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Mi credencial") },
            onClick = {
                navController.navigate("credential_view")
                navController.popBackStack() // Close drawer
            }
        )
        DrawerItem(
            label = "Mi horario",
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Mi horario") },
            onClick = {
                navController.navigate("horario_view")
                navController.popBackStack() // Close drawer
            }
        )
        DrawerItem(
            label = "Mis datos",
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Mis datos") },
            onClick = {
                navController.navigate("profile_view") // Assuming "profile_view" is your screen for user data
                navController.popBackStack() // Close drawer
            }
        )
        DrawerItem(
            label = "Acerca de",
            icon = { Icon(Icons.Filled.Info, contentDescription = "Acerca de") },
            onClick = {
                navController.navigate("about_view") // Assuming "about_view" is your screen for about information
                navController.popBackStack() // Close drawer
            }
        )
    }
}

@Composable
fun DrawerItem(label: String, icon: @Composable () -> Unit, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Spacer(modifier = Modifier.width(16.dp))
            Text(label)
        }
    }
}
