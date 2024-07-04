package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.CardTittle
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorCredentialView
import com.pixelfusion.accesio_utn.components.DrawerContent
import com.pixelfusion.accesio_utn.components.SquashedOval
import com.pixelfusion.accesio_utn.ui.theme.GreenUTN80
import com.pixelfusion.accesio_utn.ui.theme.GuindaColor
import com.pixelfusion.accesio_utn.ui.theme.WhiteColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CredentialView(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isFront by remember { mutableStateOf(true) }
    val rotation = remember { Animatable(0f) }
    val titleCard = if (isFront) "Mi credencial" else "Mi QR"

    LaunchedEffect(isFront) {
        rotation.animateTo(
            targetValue = if (isFront) 0f else 180f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(navController)
        },
        drawerState = drawerState,
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
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = titleCard,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(600.dp)
                            .clickable { isFront = !isFront }
                            .graphicsLayer {
                                rotationY = rotation.value
                                cameraDistance = 12f * density
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Ensure both cards are in the composition tree
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    alpha = if (rotation.value <= 90f) 1f else 0f
                                    rotationY = rotation.value
                                    cameraDistance = 12f * density
                                }
                        ) {
                            ContenidoFrontalCard()
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    alpha = if (rotation.value > 90f) 1f else 0f
                                    rotationY = rotation.value - 180f
                                    cameraDistance = 12f * density
                                }
                        ) {
                            ContenidoTraseroCard()
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    /*Button(
                        onClick = {
                            navController.navigate("home_user_view")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Enviar")
                    }*/
                }
            }
        }
    )
}


@Composable
fun ContenidoFrontalCard() {
    val backgroundPainter = painterResource(id = R.drawable.edomex02)

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor.copy(alpha = 0.8f) // Ajusta la opacidad si es necesario
        ),
        border = BorderStroke(1.dp, Color.Black),        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .background(WhiteColor)
        ) {
            Image(
                painter = backgroundPainter,
                contentDescription = "Fondo de la tarjeta",
                //contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                    // Para disminuir el brillo, ajusta los valores a menos de 1
                    setToScale(0.5f, 0.5f, 0.5f, 1f)
                }),
                //modifier = Modifier.fillMaxSize()
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CardTittle()
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                // Imagen de perfil
                Image(
                    painter = painterResource(id = R.drawable.valdo_pixel),
                    contentDescription = "Perfil usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(SquashedOval())
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "NOMBRE:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "VILLALBA MENDOZA OSVALDO",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "MATRICULA:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "232271007",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                // Carrera - ocupacion
                Text(
                    text = "ING. EN DESARROLLO Y GESTIÓN DE SOFTWARE",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Text(
                    text = "ESTUDIANTE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }
        }
    }
}


@Composable
fun ContenidoTraseroCard() {
    val backgroundPainter = painterResource(id = R.drawable.edomex02)

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = WhiteColor.copy(alpha = 0.8f) // Ajusta la opacidad si es necesario
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(WhiteColor)
        ) {
            Image(
                painter = backgroundPainter,
                contentDescription = "Fondo de la tarjeta",
                //contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                    // Para disminuir el brillo, ajusta los valores a menos de 1
                    setToScale(0.5f, 0.5f, 0.5f, 1f)
                }),
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Título
                CardTittle()
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                // Imagen de perfil
                Image(
                    painter = painterResource(id = R.drawable.qr_example),
                    contentDescription = "QR profile",
                    modifier = Modifier
                        .size(150.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "|||||||||||||||||||||||",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Vigencia:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "2023-2025",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "ING. EN DESARROLLO Y GESTIÓN DE SOFTWARE",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Text(
                    text = "ESTUDIANTE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
            }
        }
    }
}
