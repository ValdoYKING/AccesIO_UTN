package com.pixelfusion.accesio_utn.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.helper.PreferenceHelper
import com.pixelfusion.accesio_utn.ui.theme.utnGreen

@Composable
fun StartScreen(navController: NavHostController, context: Context) {
    val prefs = PreferenceHelper(context)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D9462)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.utnfoto),
            contentDescription = null,
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Universidad Tecnológica de Nezahualcóyotl",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Credencial Digital",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        /*Button(onClick = { navController.navigate("image_cam_view") }) {
            Text(text = "Comenzar")
        }*/
        Button(
            //color green
            colors = ButtonDefaults.buttonColors(utnGreen),
            onClick = {
                prefs.hasSeenStartScreen = true
                navController.navigate("login_screen")
                /*navController.navigate("login_screen") {
                    popUpTo("start_screen") { inclusive = true }
                }*/
            }) {
            Text(text = "Comenzar")
        }
    }
}