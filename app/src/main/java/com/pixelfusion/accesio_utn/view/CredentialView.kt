package com.pixelfusion.accesio_utn.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.CardTittle
import com.pixelfusion.accesio_utn.components.ContenidoSuperiorCredentialView
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.helper.getLocation
import com.pixelfusion.accesio_utn.logicadependencias.generateBarcodeCode128
import com.pixelfusion.accesio_utn.logicadependencias.generateBarcodeCode39
import com.pixelfusion.accesio_utn.logicadependencias.generateQRCode
import com.pixelfusion.accesio_utn.ui.theme.BackgroundCredential
import com.pixelfusion.accesio_utn.viewmodel.CredentialViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CredentialView(navController: NavController, viewModel: CredentialViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isFront by remember { mutableStateOf(true) }
    val rotation = remember { Animatable(0f) }
    val titleCard = if (isFront) "Mi credencial" else "Mi QR"
    val dataC = viewModel.state
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val context = LocalContext.current // Obtén el contexto aquí

    var location by remember { mutableStateOf<Location?>(null) }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                getLocation(context) { loc ->
                    location = loc
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.fetchData()

        // Solicitar permisos de ubicación
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getLocation(context) { loc ->
                location = loc
            }
        }
    }

    LaunchedEffect(isFront) {
        rotation.animateTo(
            targetValue = if (isFront) 0f else 180f,
            animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            //DrawerContent(navController)
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    ContenidoSuperiorCredentialView(drawerState, scope, navController)
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
                    if (viewModel.isLoading) {
                        Spacer(modifier = Modifier.height(20.dp))
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(alignment = Alignment.CenterHorizontally)
                        )
                    } else {
                        Text(
                            text = titleCard,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
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
                                ContenidoFrontalCard(viewModel)
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
                                ContenidoTraseroCard(viewModel, location)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    )
}


@Composable
fun ContenidoFrontalCard(dataC:CredentialViewModel) {
    val backgroundPainter = painterResource(id = R.drawable.edomex02)
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
            /*listOf(
                Color(0xFF9575CD),
                Color(0xFFBA68C8),
                Color(0xFFE57373),
                Color(0xFFFFB74D),
                Color(0xFFFFF176),
                Color(0xFFAED581),
                Color(0xFF4DD0E1),
                Color(0xFF9575CD)
            )*/
            listOf(
                Color(0xFFFFFFFF),
                Color(0xFFFCE4EC),
                Color(0xFFF8BBD0),
                Color(0xFFF48FB1),
                Color(0xFFF06292),
                Color(0xFFEC407A),
                Color(0xFFE91E63),
                Color(0xFFD81B60),
                Color(0xFFC2185B),
                Color(0xFFAD1457),
                Color(0xFF880E4F),
                Color(0xFF6D0A3B),
                Color(0xFF6D0A3B),
                Color(0xFF880E4F),
                Color(0xFFAD1457),
                Color(0xFFC2185B),
                Color(0xFFD81B60),
                Color(0xFFE91E63),
                Color(0xFFEC407A),
                Color(0xFFF06292),
                Color(0xFFF48FB1),
                Color(0xFFF8BBD0),
                Color(0xFFFCE4EC),
                Color(0xFFFFFFFF)
            )

        )
    }
    val borderWidth = 4.dp

    OutlinedCard(
        colors = CardDefaults.cardColors(
            //containerColor = BackgroundCredential.copy(alpha = 0.8f) // Ajusta la opacidad si es necesario
            //containerColor = backgroundColor
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)

        ),
        //border = BorderStroke(1.dp, Color.White),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color.White else Color.Black),
        //border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        ) {
            /*Image(
                painter = backgroundPainter,
                contentDescription = "Fondo de la tarjeta",
                //contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToScale(0.5f, 0.5f, 0.5f, 1f)
                }),
                //modifier = Modifier.fillMaxSize()
                modifier = Modifier.fillMaxWidth()
            )*/
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CardTittle()
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    thickness = 1.dp
                )
                // Imagen de perfil
                val imagePath = dataC.state.image_path ?: ""
                if (imagePath.isNotEmpty()) {
                    val imagePainter: Painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = imagePath)
                            .apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.app_fondo)
                            }).build()
                    )
                    Image(
                        //image_path
                        painter = imagePainter,
                        //painter = painterResource(id = R.drawable.valdo_pixel),
                        contentDescription = "Perfil usuario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp)
                            //.clip(SquashedOval())
                            .border(
                                BorderStroke(borderWidth, rainbowColorsBrush),
                                CircleShape
                            )
                            .padding(borderWidth)
                            .clip(CircleShape)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "NOMBRE:",
                        fontSize = 16.sp,
                        //fontWeight = FontWeight.Bold,
                        //color = Color.Black
                    )
                    //nombre completo
                    Text(
                        text = dataC.state.nombre + " " + dataC.state.apellido,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.Black
                    )
                    Text(
                        text = "MATRICULA:",
                        fontSize = 16.sp,
                        //fontWeight = FontWeight.Bold,
                        //color = Color.Black
                    )
                    Text(
                        text = dataC.state.matricula,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.Black
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    thickness = 1.dp
                )
                // Carrera - ocupacion
                Text(
                    text = dataC.state.carrera,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    //color = Color.Black
                )
                Text(
                    text = dataC.state.id_rol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    //color = Color.Black,
                )
            }
        }
    }
}


@Composable
fun ContenidoTraseroCard(dataC: CredentialViewModel, location: Location?) {
    val backgroundPainter = painterResource(id = R.drawable.edomex02)
    val uid = Firebase.auth.currentUser?.uid.orEmpty()
    // State para mantener la hora y la fecha actuales
    var hora by remember { mutableStateOf(getCurrentTime()) }
    var fecha by remember { mutableStateOf(getCurrentDate()) }
    val matricula = dataC.state.matricula

    // Corutina para actualizar la hora y la fecha cada 3 segundos
    LaunchedEffect(Unit) {
        while (true) {
            hora = getCurrentTime()
            fecha = getCurrentDate()
            delay(2000) // Espera 2 segundos
        }
    }

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = BackgroundCredential.copy(alpha = 0.8f) // Ajusta la opacidad si es necesario
        ),
        //border = BorderStroke(1.dp, Color.Black),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) Color.White else Color.Black),
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCredential)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Título
                CardTittle()
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    thickness = 1.dp
                )
                BarcodeImage(content = dataC.state.matricula, width = 220, height = 80)
                Text(
                    text = matricula,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
                //Codigo QR
                if (location != null) {
                    val locationString = "${location.latitude},${location.longitude}"
                    QRCode(hora, fecha, matricula, uid, locationString)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Vigencia:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    var si = "${location?.latitude},${location?.longitude}"
                    Text(
                        text = si,
                        fontSize = 14.sp,
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    thickness = 1.dp
                )
                Text(
                    text = dataC.state.carrera,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = dataC.state.id_rol,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// Función para obtener la ubicación del usuario
/*fun getLocation(context: android.content.Context, callback: (Location?) -> Unit) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // La ubicación se obtuvo exitosamente
                callback(location)
            }
            .addOnFailureListener { exception ->
                // Error al obtener la ubicación
                callback(null)
            }
    } else {
        callback(null)
    }
}*/


fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date())
}

// Función para obtener la hora actual en formato HH:mm:ss en la zona horaria UTC-6:00
/*fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timezone = TimeZone.getTimeZone("America/Mexico_City")
    sdf.timeZone = timezone
    return sdf.format(Date())
}*/

// Función para obtener la fecha actual en formato dd-MM-yyyy en la zona horaria UTC-6:00
/*fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val timezone = TimeZone.getTimeZone("America/Mexico_City")
    sdf.timeZone = timezone
    return sdf.format(Date())
}*/

@Composable
fun BarcodeImage(content: String, width: Int, height: Int) {
    val context = LocalContext.current
    var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(content) {
        barcodeBitmap = generateBarcodeCode39(content)
    }

    barcodeBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            //modifier = Modifier.size(width.dp, height.dp),
            modifier = Modifier.size(width.dp, height.dp),
            contentScale = ContentScale.Fit,
            //colorFilter = ColorFilter.tint(BlackColor)
        )
    }
}


@Composable
fun QRCodeAndBarcode(contentQR: String, qrWidth: Int = 300, qrHeight: Int = 300,) {
    val qrCodeBitmap = generateBarcodeCode128(contentQR, qrWidth, qrHeight)

    Column {
        qrCodeBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(qrWidth.dp, qrHeight.dp)
            )
        }

    }
}


@Composable
fun QRCode(
    hora: String,
    fecha: String,
    matricula: String,
    UIDUT: String,
    locationString: String,
    qrWidth: Int = 300,
    qrHeight: Int = 300
) {
    val qrCodeBitmap = generateQRCode(
        hora,
        fecha,
        matricula,
        UIDUT,
        locationString,
        qrWidth,
        qrHeight
    )
    qrCodeBitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            //modifier = Modifier.size(qrWidth.dp, qrHeight.dp)
            modifier = Modifier.size(280.dp)
        )
    }
}