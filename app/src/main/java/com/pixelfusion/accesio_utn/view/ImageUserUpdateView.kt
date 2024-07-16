package com.pixelfusion.accesio_utn.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.viewmodel.ImageUserUpdateViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUserUpdateView(navController: NavController, viewModel: ImageUserUpdateViewModel) {
    val context = LocalContext.current

    // Crear archivo y URI para la imagen
    val file = context.createImageUpdateFileUser()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    // Estados
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imageDefault = R.drawable.cam_image

    // Lógica para lanzar la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = uri
        }
    }

    // Lógica para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.showToast(context, "Permiso Concedido")
            cameraLauncher.launch(uri)
        } else {
            viewModel.showToast(context, "Permiso Denegado")
        }
    }

    // Verificar permiso
    val permissionCheckResult =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    // Interfaz de usuario
    Scaffold(
        topBar = {
            Spacer(modifier = Modifier.height(26.dp))
            TopAppBar(title = {
                Text(
                    "Agrega tu imagen 😎",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            })
        }
    ) {
        if (imageUri != null) {
            // Vista previa de la imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = {
                        // Lógica para subir la imagen
                        //imageUri?.let { viewModel.uploadImageAndUpdateUser(context, it) }
                        imageUri?.let {
                            viewModel.uploadImageAndUpdateUser(context, it) {
                                // Navigate to home_user_view after upload and update are successful
                                navController.navigate("home_user_view") {
                                    popUpTo("image_user_view") { inclusive = true }
                                }
                            }
                        }
                    }) {
                        Text("Subir")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { imageUri = null }) {
                        Text("Cancelar")
                    }
                }
            }
        } else {
            // Mostrar sugerencias y botón para capturar imagen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sugerencias para tomar la fotografía:",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val suggestions = listOf(
                    "Cara descubierta 🙂",
                    "Sin maquillaje 👩",
                    "Sin anteojos 👱‍♂️",
                    "Fondo claro 🌅",
                )

                suggestions.forEach { suggestion ->
                    SuggestionItemUpdate(suggestion)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Presiona la cámara cuando estés listo 👀",
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    modifier = Modifier
                        .clickable {
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(uri)
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                        .padding(16.dp, 8.dp),
                    painter = rememberAsyncImagePainter(imageDefault),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(16.dp))
                ButtonNext(navController, "home_user_view")
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageUpdateFileUser(): File {
    // Crea un archivo temporal en el directorio de caché
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}

@Composable
fun SuggestionItemUpdate(suggestion: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = suggestion,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
        )
    }
}
