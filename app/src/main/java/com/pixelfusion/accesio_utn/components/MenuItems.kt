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
import androidx.compose.material.icons.filled.Warning
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
//        Icon(
//            imageVector = Icons.Filled.AccountBox,
//            contentDescription = "Credencial",
//            tint = if (isSystemInDarkTheme()) BlueMarine else WhiteColor2,
//            modifier = Modifier.size(48.dp)
//        )
        val imageScanAssist = if (isSystemInDarkTheme()) {
            R.drawable.id_credential
        } else {
            R.drawable.id_credential
        }
        Image(
            painter = painterResource(id = imageScanAssist),
            contentDescription = "Credencial",
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
        val imageMyHistorialAssist = if (isSystemInDarkTheme()) {
            R.drawable.agenda1
        } else {
            R.drawable.agenda1
        }
        Image(
            painter = painterResource(id = imageMyHistorialAssist),
            contentDescription = "Agenda",
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
        verticalArrangement = Arrangement.Center,

    ) {
        val imageMyAssist = if (isSystemInDarkTheme()) {
            R.drawable.checkbox2
        } else {
            R.drawable.checkbox2
        }
        Image(
            painter = painterResource(id = imageMyAssist),
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
        val imageEscQRResource = if (isSystemInDarkTheme()) {
            R.drawable.icon_scanqr
        } else {
            R.drawable.icon_scanqr
        }
        Image(
            painter = painterResource(id = imageEscQRResource),
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
            //R.drawable.icon_qr_dark
            R.drawable.qr_code
        } else {
            R.drawable.qr_code
            //R.drawable.icons8_qr_code_100_l
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
            R.drawable.historial
        } else {
            R.drawable.historial
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
        val imageListQrsResource = if (isSystemInDarkTheme()) {
            R.drawable.group
        } else {
            R.drawable.group
        }
        Image(
            painter = painterResource(id = imageListQrsResource),
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
            R.drawable.qrubic
        } else {
            R.drawable.qrubic
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
            R.drawable.list_check
        } else {
            R.drawable.list_check
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
            R.drawable.check_list
        } else {
            R.drawable.check_list
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

@Composable
fun HistorialLugar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageUbicacionAssist = if (isSystemInDarkTheme()) {
            R.drawable.ubicacion
        } else {
            R.drawable.ubicacion
        }
        Image(
            painter = painterResource(id = imageUbicacionAssist),
            contentDescription = "Historial lugares",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Lugares",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ListaAccesosUsers() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageChecklistAccessAssist = if (isSystemInDarkTheme()) {
            R.drawable.list_ass
        } else {
            R.drawable.list_ass
        }
        Image(
            painter = painterResource(id = imageChecklistAccessAssist),
        contentDescription = "Lista accesos",
        modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Accesos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

