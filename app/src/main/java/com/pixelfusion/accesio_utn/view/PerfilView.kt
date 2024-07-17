package com.pixelfusion.accesio_utn.view

import UserProfileViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.SuperiorData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PerfilView ( navController: NavController,
viewModel: UserProfileViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


        val user = viewModel.userState

            Scaffold(
                topBar = { TopBarProfile() }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item { Spacer(modifier = Modifier.height(30.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.nombre,
                            onValueChange = { viewModel.onValueChange(it, "nombre") },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.apellido,
                            onValueChange = { viewModel.onValueChange(it, "apellido") },
                            label = { Text("Apellidos") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.matricula,
                            onValueChange = { viewModel.onValueChange(it, "matricula") },
                            label = { Text("Matrícula") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.carrera,
                            onValueChange = { viewModel.onValueChange(it, "carrera") },
                            label = { Text("Carrera") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            keyboardActions = KeyboardActions(onNext = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.correo_electronico,
                            onValueChange = { viewModel.onValueChange(it, "correo_electronico") },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                    item {
                        OutlinedTextField(
                            value = user.num_seguro_social,
                            onValueChange = { viewModel.onValueChange(it, "num_seguro_social") },
                            label = { Text(text = "Número seguro social") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { /* No action */ })
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    item {
                        Button(onClick = {
                            // Lógica para cerrar sesión
                            viewModel.logout()
                            navController.navigate("login_screen") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        }) {
                            Text(text = "Cerrar Sesión")
                        }
                    }
                }
            }
}
@Composable
fun TopBarProfile() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0D9462),
        border = BorderStroke(2.dp, Color(0xFF0D9462)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.utnfoto),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Mi Perfil",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}





