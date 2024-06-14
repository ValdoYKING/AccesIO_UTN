package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.navigation.AppScreens
import kotlinx.coroutines.delay
import com.pixelfusion.accesio_utn.components.MyApp

@Composable
fun SplashScreen(navController: NavHostController){
    LaunchedEffect(key1 = true) {
        delay(900)
        navController.popBackStack()
        navController.navigate(AppScreens.MyApp.route)
    }
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D9462)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ut_logo_super),
            contentDescription = "Logo UT",
            modifier = Modifier.size(250.dp, 250.dp) // Tamaño de la imagen aumentado
        )
        Text(
            text = "AccesIO UTN",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White // Cambiado a blanco para mejor visibilidad
        )
    }
}