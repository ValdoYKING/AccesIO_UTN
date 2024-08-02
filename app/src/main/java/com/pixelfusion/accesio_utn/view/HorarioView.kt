package com.pixelfusion.accesio_utn.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.pixelfusion.accesio_utn.components.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun HorarioView(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperior(drawerState, scope, navController)
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ButtonNext(navController, "home_user_view")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Horario",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (isLoading) {
                            ButtonCreateSchedule(navController, "create_schedule_view")
                        }
                        InfoScheduleStudent(navController)
                    }
                }
            )
        }
    )
}

@Composable
fun InfoScheduleStudent(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    var horarios by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val currentUser = Firebase.auth.currentUser
    val email = currentUser?.email ?: ""
    LaunchedEffect(Unit) {
        try {
            val result = db.collection("horarios").get().await()
            horarios = result.documents.map { it.data ?: emptyMap() }
            //Validamos el correo si no hay asociacion
             horarios = horarios.filter { it["email"] == email }



            horarios = horarios.groupBy { it["dia"] }.map { entry ->
                mapOf(
                    "dia" to entry.key,
                    "horarios" to entry.value.map { horario ->
                        mapOf(
                            "materia" to horario["materia"],
                            "inicia" to horario["inicia"],
                            "termina" to horario["termina"],
                            "id" to horario["id"]
                        )
                    }
                )
            } as List<Map<String, Any>>
            println("Horarios: $horarios")
            isLoading = false
        } catch (e: Exception) {
            println("Error getting documents: $e")
            isLoading = false
        }
    }

    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        return
    }

    if (horarios.isEmpty() || horarios.all { it.isEmpty() }) {
        ButtonCreateSchedule(navController, "create_schedule_view")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(horarios) { horario ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = horario["dia"].toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                (horario["horarios"] as List<Map<String, String>>).forEach { horarioDia ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = horarioDia["materia"].toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${horarioDia["inicia"]} - ${horarioDia["termina"]}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(onClick = {
                            scope.launch {
                                deleteSchedule(db, horarioDia["id"].toString(), horarios) { updatedHorarios ->
                                    horarios = updatedHorarios
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Eliminar horario")
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}

fun deleteSchedule(db: FirebaseFirestore, id: String, horarios: List<Map<String, Any>>, onUpdate: (List<Map<String, Any>>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            db.collection("horarios").document(id).delete().await()
            val updatedHorarios = horarios.map { h ->
                if (h["id"] != id) h else h.toMutableMap().apply { remove("id") }
                //sitiene id igual a null lo elimina
                if (h["id"] == null) h else h.toMutableMap().apply { remove("id") }

            }
            onUpdate(updatedHorarios)
        } catch (e: Exception) {
            println("Error deleting document: $e")
        }
    }
}