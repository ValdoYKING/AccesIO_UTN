package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.components.DrawerContent
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.SuperiorData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutView(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            //DrawerContent(navController)
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    SuperiorData(drawerState, scope)
                },

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFDFF2BF)) // Fondo verde claro
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()), // Habilitar el desplazamiento vertical
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))


                        // Nombre de la universidad
                        Text(
                            text = "UTN",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "¿Quiénes somos?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = " En AcceciOUTN, hemos creado una aplicación móvil innovadora para digitalizar credenciales, abordando las ineficiencias y la falta de seguridad asociadas con las credenciales físicas tradicionales. Reconocemos que portar múltiples credenciales físicas no solo es poco práctico sino también vulnerable a pérdidas, falsificaciones y usurpación de identidad, además de contribuir negativamente al medio ambiente.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify,

                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Nuestra solución:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        //puntos con viñetas
                        Column {
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Seguridad Mejorada: ")
                                    }
                                    append("Utilizamos autenticación multifactorial y protocolos avanzados para garantizar la máxima seguridad de tus credenciales.")
                                },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Conveniencia: ")
                                    }
                                    append("Transforma tus credenciales físicas en formatos digitales accesibles a través de tu dispositivo móvil, eliminando la necesidad de llevar múltiples tarjetas físicas.")
                                },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Eficiencia: ")
                                    }
                                    append("Simplificamos el proceso de gestión de credenciales, permitiendo un acceso rápido y fácil en cualquier momento y lugar.")
                                },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Sostenibilidad: ")
                                    }
                                    append("Al reducir la dependencia de materiales físicos, contribuimos significativamente a la reducción de residuos y al impacto ambiental.")
                                },
                                fontSize = 16.sp,
                                textAlign = TextAlign.Justify
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        // Valores
                        Text(
                            text = "Valores",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "En AcceciOUTN, nuestros valores fundamentales son la seguridad, la conveniencia, la eficiencia, y la sostenibilidad. Trabajamos incansablemente para ofrecer una solución que no solo haga la vida más fácil, sino que también promueva prácticas más responsables con el medio ambiente.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify // Justificación del texto
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Equipo
                        Text(
                            text = "Equipo",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Somos estudiantes del décimo cuatrimestre de la Universidad Tecnológica De Nezahualcoyotl  (UTN). Nuestro equipo está compuesto por profesionales apasionados y comprometidos con la innovación y la excelencia. Cada miembro aporta su experiencia y habilidades para desarrollar una aplicación que cumple con los más altos estándares de calidad y seguridad.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify // Justificación del texto
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Misión y visión
                        Text(
                            text = "Misión y Visión",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                ) {
                                    append("Misión: ")
                                }
                                append("Nuestra misión es transformar la manera en que se gestionan las credenciales, proporcionando una solución digital segura y eficiente que beneficie tanto a los usuarios como al medio ambiente.\n\n")
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                ) {
                                    append("Visión: ")
                                }
                                append("Aspiramos a ser líderes en la digitalización de credenciales, estableciendo nuevos estándares de seguridad y conveniencia a nivel mundial.")
                            },
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify // Justificación del texto
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        // Contacto
                        Text(
                            text = "Contacto",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Estamos aquí para ayudarte. Si tienes alguna pregunta o necesitas asistencia, no dudes en contactarnos a través de nuestras plataformas de soporte.",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Justify // Justificación del texto
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                    }
                }
            }
        }
    )
}

@Composable
fun SolutionPoint(title: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                        append("$title: ")
                    }
                    append(description)
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Justify // Justificación del texto
            )
        }
    }
}