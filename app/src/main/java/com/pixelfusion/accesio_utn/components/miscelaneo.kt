package com.pixelfusion.accesio_utn.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pixelfusion.accesio_utn.R
import kotlinx.coroutines.delay

@Composable
fun LoginScreen() {
    var emailOrId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentDescription = "Logo of the application", // Provide a meaningful description
            contentScale = ContentScale.Fit, // Adjust scaling if needed
            painter = painterResource(id = R.drawable.logo) // Use painterResource for better resource handling
        )

        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = emailOrId,
            onValueChange = { newValue ->
                emailOrId = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Correo o Matrícula") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = password,
            onValueChange = { newValue ->
                password = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { /* Acciones al iniciar sesión */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "start_screen") {
        composable("start_screen") { StartScreen(navController) }
        composable("register_screen") { RegisterScreen(navController) }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
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
        Button(onClick = { navController.navigate("register_screen") }) {
            Text(text = "Comenzar")
        }
    }
}

@Composable
fun RegisterScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.utnfoto), // reemplaza con tu logo
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "REGÍSTRATE",
            color = Color(0xFF0D9462),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val confirmPassword = remember { mutableStateOf("") }
        val socialSecurityNumber = remember { mutableStateOf("") }

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("Repite tu Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = socialSecurityNumber.value,
            onValueChange = { socialSecurityNumber.value = it },
            label = { Text("Número de seguro Social") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO: Handle registration */ }) {
            Text(text = "Enviar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { /* TODO: Handle already have an account */ }) {
            Text(text = "Ya tengo cuenta")
        }
    }
}
@Composable
fun StartScreen3(navController: NavHostController) {
    var isVisible by remember { mutableStateOf(false) }

    // La animación se ejecuta una vez que se crea la pantalla
    LaunchedEffect(Unit) {
        isVisible = true
        delay(2000) // Duración de la animación (2 segundos)
        navController.navigate("register_screen")
    }

   /* AnimatedVisibility(
        visible = isVisible,
        enter = expandIn(
            initialSize = { IntSize(50, 50) }, // Tamaño inicial pequeño
            animationSpec = tween(durationMillis = 2000) // Duración de la animación (2 segundos)
        )
    )
    */
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
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
        }
    }
}
