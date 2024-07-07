package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorCredentialView
import com.pixelfusion.accesio_utn.components.DrawerContent3

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HorarioView(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            //DrawerContent(navController)
            DrawerContent3(navController, currentRoute)
        },
        content = {

            Scaffold(
                topBar = {
                    ContenidoSuperiorCredentialView(drawerState, scope)
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mi horario (view)",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ButtonNext(navController, "home_user_view")
                }
            }

        }

    )
}
