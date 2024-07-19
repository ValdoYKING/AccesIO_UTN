package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import android.Manifest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.SuperiorData
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.model.UsuarioData
import com.pixelfusion.accesio_utn.ui.theme.blackdark
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.viewmodel.ScanQRAccessViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScanQRAccessView(navController: NavController, viewModel: ScanQRAccessViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    var isBackCamera by remember { mutableStateOf(true) }

    // Variables para controlar escaneos
    var lastScanTime by remember { mutableStateOf(0L) }
    var lastScannedQr by remember { mutableStateOf<String?>(null) }

    fun flipCamera() {
        isBackCamera = !isBackCamera
    }

    LaunchedEffect(cameraProviderFuture, hasCameraPermission, isBackCamera) {
        if (hasCameraPermission) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = if (isBackCamera) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val barcodeScanner = BarcodeScanning.getClient()

            imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                val currentTime = System.currentTimeMillis()

                // Deshabilitar escaneo si el diálogo está abierto
                if (!viewModel.isUserDetailDialogVisible && currentTime - lastScanTime > 1000) { // Espera 1 segundo entre escaneos
                    processImageProxy(barcodeScanner, imageProxy) { result ->
                        if (result != lastScannedQr) {
                            lastScannedQr = result
                            lastScanTime = currentTime
                            viewModel.updateQrCodeContent(result)
                        }
                    }
                } else {
                    imageProxy.close() // Cerrar si no se escanea
                }
            }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                // Manejar cualquier error
            }
        }
    }

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
            ) { paddingValues ->
                if (hasCameraPermission) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TopBarUT("Escanear QR de acceso")
                        Spacer(modifier = Modifier.height(16.dp))
                        AndroidView(
                            factory = { previewView },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(4.dp, Color.Green, RoundedCornerShape(16.dp))
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { flipCamera() }) {
                            Text("Voltear Cámara")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (viewModel.isLoading) {
                            CircularProgressIndicator()
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
                        Text("Se necesita permiso de cámara para escanear códigos QR")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                launcher.launch(Manifest.permission.CAMERA)
                            }
                        ) {
                            Text("Solicitar Permiso")
                        }
                    }
                }

                if (viewModel.isLoadingDialog) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                if (viewModel.isUserDetailDialogVisible) {
                    UserDetailDialog(
                        user = viewModel.user,
                        onDismiss = { viewModel.dismissUserDetailDialog() }
                    )
                }
            }
        }
    )
}


@Composable
fun UserDetailDialog(user: UsuarioData, onDismiss: () -> Unit) {
    val rainbowColorsBrush = remember {
        Brush.sweepGradient(
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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = user.nombre + " " + user.apellido,
                textAlign = TextAlign.Center, // Center title
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center column content
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp) // Add padding to the column
            ) {
                Image(
                    painter = rememberAsyncImagePainter(user.image_path),
                    contentDescription = "Imagen usuario",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(180.dp * 1.15f) // Increase size by 15%
                        .border(
                            BorderStroke(borderWidth, rainbowColorsBrush),
                            CircleShape
                        )
                        .padding(borderWidth)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Center the text "Rol:" and the id_rol
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth() // Fill the width for proper centering
                ) {
                    Text(
                        text = "Rol: ",
                        fontSize = 18.sp * 1.15f // Increase text size by 15%
                    )
                    Text(
                        text = user.id_rol,
                        fontSize = 18.sp * 1.15f // Increase text size by 15%
                    )
                }
            }
        },
        confirmButton = {
            /*Button(onClick = onDismiss) {
                Text("OK")
            }*/
        }
    )
}


@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onQrCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT -> onQrCodeScanned(barcode.displayValue ?: "")
                        // Handle other types of QR codes here
                        else -> onQrCodeScanned("QR code type not supported")
                    }
                }
            }
            .addOnFailureListener {
                onQrCodeScanned("Error scanning QR code")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}