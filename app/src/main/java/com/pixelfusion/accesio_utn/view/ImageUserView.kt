package com.pixelfusion.accesio_utn.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.model.extractStudentInfo
import com.pixelfusion.accesio_utn.viewmodel.ImageUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScannerViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUserView(navController: NavController, viewModel: ImageUserViewModel) {
    val context = LocalContext.current

    // Crear archivo y URI para la imagen
    val file = context.createImageFileUser()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    // Estados
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imageDefault = R.drawable.photo

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
                Text("Sugerencias para tomar la fotografía:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Cara descubierta")
                Text("2. Sin maquillaje")
                Text("3. Sin anteojos")

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
fun Context.createImageFileUser(): File {
    // Crea un archivo temporal en el directorio de caché
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: filesDir
    return File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        storageDir
    )
}


/*
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageUserView(navController: NavController, viewModel: ImageUserViewModel) {
    val context = LocalContext.current

    val file = context.createImageFileUser()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imageDefault = R.drawable.photo

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = uri
        }
    }

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

    val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

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
                        imageUri?.let {
                            viewModel.uploadImageAndUpdateUser(context, it) {
                                viewModel.setImageUri(it) // Establece la URI de la imagen en el ViewModel
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Sugerencias para tomar la fotografía:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. Cara descubierta")
                Text("2. Sin maquillaje")
                Text("3. Sin anteojos")

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
}*/
