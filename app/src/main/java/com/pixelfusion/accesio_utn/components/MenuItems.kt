package com.pixelfusion.accesio_utn.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.ui.theme.BlueMarine
import com.pixelfusion.accesio_utn.ui.theme.WhiteColor2

@Composable
fun CredencialMenuItem() {
    Column(
        modifier = Modifier
            //.fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.AccountBox,
            contentDescription = "Credencial",
            tint = if (isSystemInDarkTheme()) BlueMarine else WhiteColor2,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // Added vertical spacing
        Text(
            text = "Credencial",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HorarioMenuItem() {
    Column(
        modifier = Modifier
            //.fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.DateRange,
            contentDescription = "Horarios",
            tint = if (isSystemInDarkTheme()) BlueMarine else WhiteColor2,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Horarios",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MiAsistenciaMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageScanAssist = if (isSystemInDarkTheme()) {
            R.drawable.icon_qr_scan_dark
        } else {
            R.drawable.icon_qr_scan_light
        }
        Image(
            painter = painterResource(id = imageScanAssist),
            contentDescription = "Escanear asistencia",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Mi asistencia",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EscanearQRMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageQRResource = if (isSystemInDarkTheme()) {
            R.drawable.icon_qr_scan_dark
        } else {
            R.drawable.icon_qr_scan_light
        }
        Image(
            painter = painterResource(id = imageQRResource),
            contentDescription = "Escanear QR seguridad ",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // Added vertical spacing
        Text(
            text = "Escanear QR",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GenerarQRMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageGenerateQrResource = if (isSystemInDarkTheme()) {
            R.drawable.icon_qr_dark
        } else {
            R.drawable.icons8_qr_code_100_l
        }
        Image(
            painter = painterResource(id = imageGenerateQrResource),
            contentDescription = "Generar QR lugar",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Generar QR",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HistorialMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageHistorialResource = if (isSystemInDarkTheme()) {
            R.drawable.historial_dark
        } else {
            R.drawable.historial_light
        }
        Image(
            painter = painterResource(id = imageHistorialResource),
            contentDescription = "Historial entradas y salidas",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Historial",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListaQRMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageChecklistResource = if (isSystemInDarkTheme()) {
            R.drawable.icon_checklist_dark
        } else {
            R.drawable.icon_checklist_light
        }
        Image(
            painter = painterResource(id = imageChecklistResource),
            contentDescription = "Lista QR's",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Mis QR's",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EscanearQRLugarMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageQRPlace = if (isSystemInDarkTheme()) {
            R.drawable.icon_qr_scan_dark
        } else {
            R.drawable.icon_qr_scan_light
        }
        Image(
            painter = painterResource(id = imageQRPlace),
            contentDescription = "Escanear QR lugar",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "QR Lugar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MiHistorialAsistencia() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageHistorialAssist = if (isSystemInDarkTheme()) {
            R.drawable.historial_dark
        } else {
            R.drawable.historial_light
        }
        Image(
            painter = painterResource(id = imageHistorialAssist),
            contentDescription = "Historial asistencias",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Mis asistencias",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListaAsistenciaAlumnos() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageChecklistHistoryAssist = if (isSystemInDarkTheme()) {
            R.drawable.icon_checklist_dark
        } else {
            R.drawable.icon_checklist_light
        }
        Image(
            painter = painterResource(id = imageChecklistHistoryAssist),
            contentDescription = "Lista asistencias",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Asistencias",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}