package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import org.jetbrains.annotations.Async.Schedule

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
                    ContenidoSuperior(drawerState, scope, navController)
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Horario",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    ScheduleStudent()
                    ButtonNext(navController, "home_user_view")
                }

            }


        }

    )
}


@Composable
fun ScheduleStudent() {
    val daysOfWeek = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes")
    val dataDays = listOf("Hora", "Materia")
    // Usar LazyColumn para organizar los días verticalmente y permitir el desplazamiento
    LazyColumn(
    ) {
        items(daysOfWeek) { day ->
            Text(
                text = day,
                modifier = Modifier.padding(4.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold

            )

            // Dibuja una línea de separación después de cada día
            HorizontalDivider(
                thickness = 2.dp, // Puedes cambiar el grosor de la línea aquí
               //AGREGAR COLOR PRIMARY
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Lista de datos estáticos para cada día
            Row (    modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly) {
                dataDays.forEach { data ->
                    Text(
                        text = data,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(4.dp)

                    )
                }
            }
            InfoScheduleStudent()
        }
    }
}


@Composable
fun InfoScheduleStudent() {
    // Lista de horarios estáticos

    val scheduleList = listOf(
        "08:00 - 10:00 Clase de Matemáticas",
        "10:15 - 12:15 Clase de Física",
        "12:30 - 14:30 Clase de Química"
    )


    // Organizar los horarios verticalmente
    Column(modifier = Modifier
        .fillMaxWidth())

    {
        scheduleList.forEach { schedule ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //Border radius
                    .padding(2.dp),

            // Opcional: para redondear las esquinas
                  // Opcional: para llenar todo el ancho disponible
            ) {

                    Text(
                        text = schedule,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black // Opcional: cambiar el color del texto
                    )

            }
        }
    }
}