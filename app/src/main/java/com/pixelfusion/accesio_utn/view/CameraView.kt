package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.model.StudentInfo
import com.pixelfusion.accesio_utn.model.extractStudentInfo
import com.pixelfusion.accesio_utn.viewmodel.ScannerViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.math.min

@Composable
fun CameraView(viewModel: ScannerViewModel, navController: NavController, param: (Any) -> Unit) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current


    val careers = listOf(
        "MAESTRÍA EN GESTIÓN E INNOVACIÓN DE LAS ORGANIZACIONES",
        "MAESTRÍA EN SISTEMAS DE GESTIÓN AMBIENTAL",
        "MAESTRÍA EN INNOVACIÓN Y NEGOCIOS",
        "TSU en TI Infraestructura de Redes Digitales",
        "TSU en TI Desarrollo de Software Multiplataforma",
        "TSU en Mecatrónica Área Sistemas de Manufactura Flexible",
        "TSU en Desarrollo de Negocios Área Mercadotecnia",
        "TSU en Química Área Ambiental",
        "TSU en TI Entornos Virtuales y Negocios Digitales",
        "TSU en Administración Área Capital Humano",
        "TSU en Procesos Industriales Área Manufactura",
        "TSU en Mantenimiento Aeronáutico Área Aviónica",
        "ING. EN MECATRÓNICA",
        "ING. EN TECNOLOGIÁS DE LA PRODUCCIÓN",
        "ING. EN DESARROLLO Y GESTIÓN DE SOFTWARE",
        "ING. EN REDES INTELIGENTES Y CIBERSEGURIDAD",
        "ING. EN ENTORNOS VIRTUALES Y NEGOCIOS",
        "ING. EN TECNOLOGÍA AMBIENTAL",
        "LIC. EN GESTIÓN DEL CAPITAL HUMANO",
        "LIC EN INNOVACIÓN DE NEGOCIOS Y MERCADOTECNIA"
    )

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var image by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val imageDefault = R.drawable.photo

    val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ){
        image = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){
        if (it != null){
            viewModel.showToast(context,"Permiso Concedido")
            cameraLauncher.launch(uri)
        }else{
            viewModel.showToast(context,"Permiso Denegado")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .clickable {
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED){
                        cameraLauncher.launch(uri)
                    }else{
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    }
                }
                .padding(16.dp, 8.dp),
            painter = rememberAsyncImagePainter(if (image.path?.isNotEmpty() == true) image else imageDefault),
            contentDescription = null
        ).apply {
            viewModel.onRecognizedText(image, context)
        }

        Spacer(modifier = Modifier.height(25.dp))

        val scrollState = rememberScrollState()
        val alumnoData = viewModel.recognizedText
        Text(text = viewModel.recognizedText,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(scrollState)
                .clickable {
                    clipboard.setText(AnnotatedString(viewModel.recognizedText))
                    viewModel.showToast(context,"Copiado")
                }
        )
        val studentInfo01 = extractStudentInfo(alumnoData, careers)
        DisplayStudentInfo(studentInfo01)
        ButtonNext(navController, "form_register_view")

    }

}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}


// Function to calculate Levenshtein distance
fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
    val lhsLength = lhs.length
    val rhsLength = rhs.length

    var cost = Array(lhsLength + 1) { it }
    var newCost = Array(lhsLength + 1) { 0 }

    for (i in 1..rhsLength) {
        newCost[0] = i
        for (j in 1..lhsLength) {
            val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1
            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }
        val swap = cost
        cost = newCost
        newCost = swap
    }
    return cost[lhsLength]
}

// Function to find the best match from a list of careers
fun findBestMatch(text: String, careers: List<String>): String {
    return careers.minByOrNull { levenshtein(text, it) } ?: ""
}

@Composable
fun DisplayStudentInfo(studentInfo: StudentInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        studentInfo.name?.let {
            Text(text = "Nombre:")
            Text(text = it, style = MaterialTheme.typography.titleLarge)
        }

        studentInfo.matricula?.let {
            Text(text = "Matrícula:")
            Text(text = it, style = MaterialTheme.typography.titleLarge)
        }

        studentInfo.career?.let {
            Text(text = "La carrera más similar es:")
            Text(text = it, style = MaterialTheme.typography.titleLarge)
        }
    }
}



