@file:Suppress("DEPRECATION")

package com.pixelfusion.accesio_utn.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.helper.getLocation
import com.pixelfusion.accesio_utn.ui.theme.utnGreen
import com.pixelfusion.accesio_utn.ui.theme.utnGreenDark
import com.pixelfusion.accesio_utn.ui.theme.utnGreenLightWhite
import com.pixelfusion.accesio_utn.ui.theme.utnGreenSuperLight
import com.pixelfusion.accesio_utn.viewmodel.GenerateQrCodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GenerateQrView(navController: NavController, viewModel: GenerateQrCodeViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val dataAsistencia = viewModel.qrAsistencia
    val dataLugar = viewModel.qrLugar
    var tipoQR by remember { mutableStateOf("") }
    var location by remember { mutableStateOf<Location?>(null) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf<String?>(null) }
    var expandedDuracion by remember { mutableStateOf(false) }
    var selectedDuracion by remember { mutableStateOf<String?>(null) }
    var expandedDivision by remember { mutableStateOf(false) }
    var selectedDivision by remember { mutableStateOf<String?>(null) }
    var expandedMateria by remember { mutableStateOf(false) }
    //var selectedMateria by remember { mutableStateOf<String?>(null) }
    var expandedLugar by remember { mutableStateOf(false) }
    var selectedLugar by remember { mutableStateOf<String?>(null) }
    var expandedTipoLugar by remember { mutableStateOf(false) }
    var selectedTipoLugar by remember { mutableStateOf<String?>(null) }
    var expandedLugarEdificio by remember { mutableStateOf(false) }
    var selectedLugarEdificio by remember { mutableStateOf<String?>(null) }
    var expandedLugarSalon by remember { mutableStateOf(false) }
    //var selectedLugarSalon by remember { mutableStateOf<String?>(null) }
    var expandedLugarLaboratorio by remember { mutableStateOf(false) }
    var selectedLugarLaboratorio by remember { mutableStateOf<String?>(null) }
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                getLocation(context) { loc ->
                    location = loc
                    loc?.let {
                        viewModel.updateLocation(it.latitude, it.longitude)
                    }
                }
            }
        }
    )
    LaunchedEffect(Unit) {
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
                loc?.let {
                    viewModel.updateLocation(it.latitude, it.longitude)
                }
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent3(navController, currentRoute) },
        content = {
            Scaffold(
                topBar = { ContenidoSuperior(drawerState, scope, navController) },
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        //.padding(paddingValues)
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.Center
                ) {
                    TopBarUT("Generar QR")
                    Spacer(modifier = Modifier.height(16.dp))
                    // Tipo de QR
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            //value = selectedText,
                            value = selectedText ?: "",
                            //onValueChange = { selectedText = it },
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Tipo de QR") },
                            placeholder = { Text("Seleccione...") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                        ) {
                            viewModel.suggestionsQR.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedText = item
                                        tipoQR = item
                                        expanded = false
                                    },
                                )
                            }
                        }
                    }
                    if (tipoQR == "Asistencia") {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    // Asistencia
                    if (tipoQR == "Asistencia") {
                        OutlinedTextField(
                            value = dataAsistencia.titulo,
                            onValueChange = { viewModel.onValueAsistencia(it, "titulo") },
                            label = { Text("Titulo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Duracion
                        ExposedDropdownMenuBox(
                            expanded = expandedDuracion,
                            onExpandedChange = { expandedDuracion = !expandedDuracion }
                        ) {
                            TextField(
                                //value = dataAsistencia.duracion,
                                value = selectedDuracion ?: "",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Duración") },
                                placeholder = { Text("Seleccione...") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDuracion) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDuracion,
                                onDismissRequest = { expandedDuracion = false },
                                modifier = Modifier
                                    .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                            ) {
                                viewModel.suggestionsAsistenciaDuracion.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = "$item horas") },
                                        onClick = {
                                            selectedDuracion = item
                                            viewModel.onValueAsistencia(item, "duracion")
                                            expandedDuracion = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Division
                        ExposedDropdownMenuBox(
                            expanded = expandedDivision,
                            onExpandedChange = { expandedDivision = !expandedDivision }
                        ) {
                            TextField(
                                value = selectedDivision ?: "",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("División") },
                                placeholder = { Text("Seleccione...") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDivision) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDivision,
                                onDismissRequest = { expandedDivision = false },
                                modifier = Modifier
                                    .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                            ) {
                                viewModel.suggestionsAsistenciaDivision.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedDivision = item
                                            viewModel.onValueAsistencia(item, "division")
                                            expandedDivision = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Materia
                        ExposedDropdownMenuBox(
                            expanded = expandedMateria,
                            onExpandedChange = { expandedMateria = !expandedMateria }
                        ) {
                            TextField(
                                //value = selectedMateria ?: "",
                                value = dataAsistencia.materia,
                                onValueChange = { viewModel.onValueAsistencia(it, "materia") },
                                readOnly = false,
                                label = { Text("Materia") },
                                placeholder = { Text("Seleccione...") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMateria) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMateria,
                                onDismissRequest = { expandedMateria = false },
                                modifier = Modifier
                                    .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                            ) {
                                viewModel.suggestionsAsistenciaMateria.forEach { item ->
                                    var juan = dataAsistencia.materia
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            //selectedMateria = item
                                            juan = item
                                            viewModel.onValueAsistencia(item, "materia")
                                            expandedMateria = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Lugar
                        ExposedDropdownMenuBox(
                            expanded = expandedLugar,
                            onExpandedChange = { expandedLugar = !expandedLugar }
                        ) {
                            TextField(
                                value = selectedLugar ?: "",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Lugar") },
                                placeholder = { Text("Seleccione...") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLugar) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedLugar,
                                onDismissRequest = { expandedLugar = false },
                                modifier = Modifier
                                    .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                            ) {
                                viewModel.suggestionsAsistenciaLugar.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedLugar = item
                                            viewModel.onValueAsistencia(item, "lugar")
                                            expandedLugar = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        // Descripcion Lugar
                        if (dataAsistencia.lugar == "Laboratorio") {
                            OutlinedTextField(
                                value = dataAsistencia.descripcion_lugar,
                                onValueChange = {
                                    viewModel.onValueAsistencia(
                                        it,
                                        "descripcion_lugar"
                                    )
                                },
                                label = { Text("Nombre del laboratorio") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                        }
                        if (dataAsistencia.lugar == "Salon") {
                            OutlinedTextField(
                                value = dataAsistencia.descripcion_lugar,
                                onValueChange = {
                                    viewModel.onValueAsistencia(
                                        it,
                                        "descripcion_lugar"
                                    )
                                },
                                label = { Text("Nombre del salon") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                maxLines = 1,

                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    // LUGAR --------------------------------------------------------------->
                    if (tipoQR == "Lugar") {
                        // Titulo lugar
                        OutlinedTextField(
                            value = dataLugar.titulo,
                            onValueChange = { viewModel.onValueLugar(it, "titulo") },
                            label = { Text("Titulo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Tipo
                        ExposedDropdownMenuBox(
                            expanded = expandedTipoLugar,
                            onExpandedChange = { expandedTipoLugar = !expandedTipoLugar }
                        ) {
                            TextField(
                                value = selectedTipoLugar ?: "",
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Tipo") },
                                placeholder = { Text("Seleccione...") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoLugar) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = expandedTipoLugar,
                                onDismissRequest = { expandedTipoLugar = false },
                                modifier = Modifier
                                    .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                            ) {
                                viewModel.suggestionsLugar.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedTipoLugar = item
                                            viewModel.onValueLugar(item, "tipo")
                                            expandedTipoLugar = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Lugar especifico
                        if (dataLugar.tipo == "Edificio") {
                            ExposedDropdownMenuBox(
                                expanded = expandedLugarEdificio,
                                onExpandedChange = {
                                    expandedLugarEdificio = !expandedLugarEdificio
                                }
                            ) {
                                TextField(
                                    value = selectedLugarEdificio ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Lugar") },
                                    placeholder = { Text("Seleccione...") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedLugarEdificio
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedLugarEdificio,
                                    onDismissRequest = { expandedLugarEdificio = false },
                                    modifier = Modifier
                                        .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                                ) {
                                    viewModel.suggestionsLugarEdificio.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                selectedLugarEdificio = item
                                                viewModel.onValueLugar(item, "lugar")
                                                expandedLugarEdificio = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (dataLugar.tipo == "Salon") {
                            ExposedDropdownMenuBox(
                                expanded = expandedLugarSalon,
                                onExpandedChange = { expandedLugarSalon = !expandedLugarSalon }
                            ) {
                                TextField(
                                    //value = selectedLugarSalon ?: "",
                                    value = dataLugar.lugar,
                                    //onValueChange = { },
                                    onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                    readOnly = false,
                                    label = { Text("Lugar") },
                                    placeholder = { Text("Seleccione...") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedLugarSalon
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedLugarSalon,
                                    onDismissRequest = { expandedLugarSalon = false },
                                    modifier = Modifier
                                        .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                                ) {
                                    viewModel.suggestionsLugarSalon.forEach { item ->
                                        var itemLugarA = dataLugar.lugar
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                //selectedLugarSalon = item
                                                itemLugarA = item
                                                viewModel.onValueLugar(itemLugarA, "lugar")
                                                expandedLugarSalon = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (dataLugar.tipo == "Laboratorio") {
                            ExposedDropdownMenuBox(
                                expanded = expandedLugarLaboratorio,
                                onExpandedChange = {
                                    expandedLugarLaboratorio = !expandedLugarLaboratorio
                                }
                            ) {
                                TextField(
                                    value = selectedLugarLaboratorio ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Lugar") },
                                    placeholder = { Text("Seleccione...") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedLugarLaboratorio
                                        )
                                    },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedLugarLaboratorio,
                                    onDismissRequest = { expandedLugarLaboratorio = false },
                                    modifier = Modifier
                                        .background(if (isSystemInDarkTheme()) utnGreen else utnGreenLightWhite)
                                ) {
                                    viewModel.suggestionsLugarLaboratorio.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(text = item) },
                                            onClick = {
                                                selectedLugarLaboratorio = item
                                                viewModel.onValueLugar(item, "lugar")
                                                expandedLugarLaboratorio = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (dataLugar.tipo == "Cubiculo") {
                            OutlinedTextField(
                                value = dataLugar.lugar,
                                onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                label = { Text("Lugar") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                        }
                        if (dataLugar.tipo == "Otro") {
                            OutlinedTextField(
                                value = dataLugar.lugar,
                                onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                label = { Text("Lugar") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = if (isSystemInDarkTheme()) utnGreenDark else utnGreenSuperLight
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    // Button
                    Spacer(modifier = Modifier.height(16.dp))
                    // Botón para registrar QR
                    Button(
                        onClick = {
                            if (tipoQR.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Seleccione un tipo de QR",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                when (tipoQR) {
                                    "Asistencia" ->
                                        if (dataAsistencia.titulo.isNotEmpty()
                                            && dataAsistencia.duracion.isNotEmpty()
                                            && dataAsistencia.division.isNotEmpty()
                                            && dataAsistencia.materia.isNotEmpty()
                                            && dataAsistencia.lugar.isNotEmpty()
                                            && dataAsistencia.descripcion_lugar.isNotEmpty()
                                        ) {
                                            viewModel.registrarQrAsistencia(navController)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Por favor, complete todos los campos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    "Lugar" ->
                                        if (dataLugar.titulo.isNotEmpty()
                                            && dataLugar.tipo.isNotEmpty()
                                            && dataLugar.lugar.isNotEmpty()
                                        ) {
                                            viewModel.registrarQrLugar(navController)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Por favor, complete todos los campos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    else -> {}
                                }
                            }

                        },
                        enabled = !viewModel.isLoading
                    ) {
                        Text("Generar QR")
                    }
                    if (viewModel.isLoadingDialog) {
                        Dialog(onDismissRequest = { /*showLoadingDialog = false*/ }) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    //Termina boton para registrar
                }
            }
        }
    )
}