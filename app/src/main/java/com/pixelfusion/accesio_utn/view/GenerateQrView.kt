package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pixelfusion.accesio_utn.components.ContenidoSuperior
import com.pixelfusion.accesio_utn.components.DrawerContent3
import com.pixelfusion.accesio_utn.components.TopBarUT
import com.pixelfusion.accesio_utn.viewmodel.GenerateQrCodeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GenerateQrView(navController: NavController, viewModel: GenerateQrCodeViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // ViewModel data
    val dataAsistencia = viewModel.qrAsistencia
    val dataLugar = viewModel.qrLugar
    var tipoQR by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(viewModel.isLoading) }

    // Expanded states and textfield sizes
    val expandedStates = remember {
        mutableStateMapOf<String, Boolean>().apply {
            putAll(
                mapOf(
                    "tipoQR" to false,
                    "duracion" to false,
                    "division" to false,
                    "materia" to false,
                    "lugarAsistencia" to false,
                    "tipoLugar" to false,
                    "lugarEdificio" to false,
                    "lugarSalon" to false,
                    "lugarLaboratorio" to false
                )
            )
        }
    }
    val textfieldSizes = remember {
        mutableStateMapOf<String, Size>().apply {
            putAll(
                mapOf(
                    "tipoQR" to Size.Zero,
                    "duracion" to Size.Zero,
                    "division" to Size.Zero,
                    "materia" to Size.Zero,
                    "lugarAsistencia" to Size.Zero,
                    "tipoLugar" to Size.Zero,
                    "lugarEdificio" to Size.Zero,
                    "lugarSalon" to Size.Zero,
                    "lugarLaboratorio" to Size.Zero
                )
            )
        }
    }

    // Suggestions lists
    val suggestionsQR = listOf("Asistencia", "Lugar")
    val suggestionsAsistenciaDivision = listOf(
        "Telematica",
        "Informatica",
        "Comercializacion",
        "Administracion",
        "Avionatica",
    )
    val suggestionsAsistenciaDuracion = listOf("1", "2", "3", "4", "5")
    val suggestionsAsistenciaMateria = listOf("MATERIA 1", "MATERIA 2", "MATERIA 3")
    val suggestionsAsistenciaLugar = listOf("Laboratorio", "Salon")
    val suggestionsLugar = listOf(
        "Edificio",
        "Salon",
        "Laboratorio",
        "Cubiculo",
        "Otro"
    )
    val suggestionsLugarEdificio = listOf(
        "Biblioteca",
        "Servicios escolares",
        "Gimnasio",
        "Piscina",
        "Estadio",
    )
    val suggestionsLugarSalon = listOf("101", "102", "103", "104", "105")
    val suggestionsLugarLaboratorio = listOf(
        "Laboratorio de informatica",
        "Laboratorio de telematica",
    )

    // Icons
    val icon = if (expandedStates["tipoQR"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconDuracion = if (expandedStates["duracion"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconDivision = if (expandedStates["division"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconMateria = if (expandedStates["materia"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconLugarAsistencia = if (expandedStates["lugarAsistencia"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconTipoLugar = if (expandedStates["tipoLugar"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconLugarEdificio = if (expandedStates["lugarEdificio"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconLugarSalon = if (expandedStates["lugarSalon"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val iconLugarLaboratorio = if (expandedStates["lugarLaboratorio"] == true)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val focusManager = LocalFocusManager.current

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
                    OutlinedTextField(
                        value = tipoQR,
                        onValueChange = { tipoQR = it },
                        modifier = Modifier
                            //.padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textfieldSizes["tipoQR"] = coordinates.size.toSize()
                            },
                        label = { Text("Tipo de QR") },
                        trailingIcon = {
                            Icon(
                                icon,
                                "contentDescription",
                                Modifier.clickable {
                                    expandedStates["tipoQR"] = !expandedStates["tipoQR"]!!
                                }
                            )
                        },
                        readOnly = true
                    )
                    DropdownMenu(
                        expanded = expandedStates["tipoQR"]!!,
                        onDismissRequest = { expandedStates["tipoQR"] = false },
                        modifier = Modifier
                            .width(
                                with(LocalDensity.current) {
                                    (textfieldSizes["tipoQR"]!!.width * 0.9f).toDp()
                                }
                            )
                            //.align(Alignment.Top)
                            //.align(Alignment.Top)
                            .align(Alignment.Start)
                    ) {
                        suggestionsQR.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label) },
                                onClick = {
                                    tipoQR = label
                                    expandedStates["tipoQR"] = false
                                }
                            )
                        }
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
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Duracion
                        OutlinedTextField(
                            value = dataAsistencia.duracion,
                            onValueChange = { viewModel.onValueAsistencia(it, "duracion") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textfieldSizes["duracion"] = coordinates.size.toSize()
                                },
                            label = { Text("Duracion") },
                            trailingIcon = {
                                Icon(
                                    iconDuracion,
                                    "contentDescription",
                                    Modifier.clickable {
                                        expandedStates["duracion"] = !expandedStates["duracion"]!!
                                    }
                                )
                            },
                            readOnly = true
                        )
                        DropdownMenu(
                            expanded = expandedStates["duracion"]!!,
                            onDismissRequest = { expandedStates["duracion"] = false },
                            modifier = Modifier
                                .width(
                                    with(LocalDensity.current) {
                                        (textfieldSizes["duracion"]!!.width * 0.9f).toDp()
                                    }
                                )
                                .align(Alignment.Start) // Agrega este modificador
                        ) {
                            suggestionsAsistenciaDuracion.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label + " horas") },
                                    onClick = {
                                        viewModel.onValueAsistencia(label, "duracion")
                                        expandedStates["duracion"] = false
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Division
                        OutlinedTextField(
                            value = dataAsistencia.division,
                            onValueChange = { viewModel.onValueAsistencia(it, "division") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textfieldSizes["division"] = coordinates.size.toSize()
                                },
                            label = { Text("Division") },
                            trailingIcon = {
                                Icon(
                                    iconDivision,
                                    "contentDescription",
                                    Modifier.clickable {
                                        expandedStates["division"] = !expandedStates["division"]!!
                                    }
                                )
                            },
                            readOnly = true
                        )
                        DropdownMenu(
                            expanded = expandedStates["division"]!!,
                            onDismissRequest = { expandedStates["division"] = false },
                            modifier = Modifier
                                .width(
                                    with(LocalDensity.current) {
                                        (textfieldSizes["division"]!!.width * 0.9f).toDp()
                                    }
                                )
                                .align(Alignment.Start) // Agrega este modificador
                        ) {
                            suggestionsAsistenciaDivision.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        viewModel.onValueAsistencia(label, "division")
                                        expandedStates["division"] = false
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Materia
                        OutlinedTextField(
                            value = dataAsistencia.materia,
                            onValueChange = { viewModel.onValueAsistencia(it, "materia") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textfieldSizes["materia"] = coordinates.size.toSize()
                                },
                            label = { Text("Materia") },
                            trailingIcon = {
                                Icon(
                                    iconMateria,
                                    "contentDescription",
                                    Modifier.clickable {
                                        expandedStates["materia"] = !expandedStates["materia"]!!
                                    }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expandedStates["materia"]!!,
                            onDismissRequest = { expandedStates["materia"] = false },
                            modifier = Modifier
                                .width(
                                    with(LocalDensity.current) {
                                        (textfieldSizes["materia"]!!.width * 0.9f).toDp()
                                    }
                                )
                                .align(Alignment.Start) // Agrega este modificador
                        ) {
                            suggestionsAsistenciaMateria.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        viewModel.onValueAsistencia(label, "materia")
                                        expandedStates["materia"] = false
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Lugar
                        OutlinedTextField(
                            value = dataAsistencia.lugar,
                            onValueChange = { viewModel.onValueAsistencia(it, "lugar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textfieldSizes["lugarAsistencia"] = coordinates.size.toSize()
                                },
                            label = { Text("Lugar") },
                            trailingIcon = {
                                Icon(
                                    iconLugarAsistencia,
                                    "contentDescription",
                                    Modifier.clickable {
                                        expandedStates["lugarAsistencia"] =
                                            !expandedStates["lugarAsistencia"]!!
                                    }
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expandedStates["lugarAsistencia"]!!,
                            onDismissRequest = { expandedStates["lugarAsistencia"] = false },
                            modifier = Modifier
                                .width(
                                    with(LocalDensity.current) {
                                        (textfieldSizes["lugarAsistencia"]!!.width * 0.9f).toDp()
                                    }
                                )
                                .align(Alignment.Start) // Agrega este modificador
                        ) {
                            suggestionsAsistenciaLugar.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        viewModel.onValueAsistencia(label, "lugar")
                                        expandedStates["lugarAsistencia"] = false
                                    }
                                )
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
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    // Lugar
                    if (tipoQR == "Lugar") {
                        // Titulo
                        OutlinedTextField(
                            value = dataLugar.titulo,
                            onValueChange = { viewModel.onValueLugar(it, "titulo") },
                            label = { Text("Titulo") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            maxLines = 1,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Tipo
                        OutlinedTextField(
                            value = dataLugar.tipo,
                            onValueChange = { viewModel.onValueLugar(it, "tipo") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textfieldSizes["tipoLugar"] = coordinates.size.toSize()
                                },
                            label = { Text("Tipo") },
                            trailingIcon = {
                                Icon(
                                    iconTipoLugar,
                                    "contentDescription",
                                    Modifier.clickable {
                                        expandedStates["tipoLugar"] = !expandedStates["tipoLugar"]!!
                                    }
                                )
                            },
                            readOnly = true
                        )
                        DropdownMenu(
                            expanded = expandedStates["tipoLugar"]!!,
                            onDismissRequest = { expandedStates["tipoLugar"] = false },
                            modifier = Modifier
                                .width(
                                    with(LocalDensity.current) {
                                        (textfieldSizes["tipoLugar"]!!.width * 0.9f).toDp()
                                    }
                                )
                                .align(Alignment.Start) // Agrega este modificador
                        ) {
                            suggestionsLugar.forEach { label ->
                                DropdownMenuItem(
                                    text = { Text(text = label) },
                                    onClick = {
                                        viewModel.onValueLugar(label, "tipo")
                                        expandedStates["tipoLugar"] = false
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // Lugar especifico
                        if (dataLugar.tipo == "Edificio") {
                            OutlinedTextField(
                                value = dataLugar.lugar,
                                onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        textfieldSizes["lugarEdificio"] = coordinates.size.toSize()
                                    },
                                label = { Text("Lugar") },
                                trailingIcon = {
                                    Icon(
                                        iconLugarEdificio,
                                        "contentDescription",
                                        Modifier.clickable {
                                            expandedStates["lugarEdificio"] =
                                                !expandedStates["lugarEdificio"]!!
                                        }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expandedStates["lugarEdificio"]!!,
                                onDismissRequest = { expandedStates["lugarEdificio"] = false },
                                modifier = Modifier
                                    .width(
                                        with(LocalDensity.current) {
                                            (textfieldSizes["lugarEdificio"]!!.width * 0.9f).toDp()
                                        }
                                    )
                                    .align(Alignment.Start) // Agrega este modificador
                            ) {
                                suggestionsLugarEdificio.forEach { label ->
                                    DropdownMenuItem(
                                        text = { Text(text = label) },
                                        onClick = {
                                            viewModel.onValueLugar(label, "lugar")
                                            expandedStates["lugarEdificio"] = false
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (dataLugar.tipo == "Salon") {
                            OutlinedTextField(
                                value = dataLugar.lugar,
                                onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        textfieldSizes["lugarSalon"] = coordinates.size.toSize()
                                    },
                                label = { Text("Lugar") },
                                trailingIcon = {
                                    Icon(
                                        iconLugarSalon,
                                        "contentDescription",
                                        Modifier.clickable {
                                            expandedStates["lugarSalon"] =
                                                !expandedStates["lugarSalon"]!!
                                        }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expandedStates["lugarSalon"]!!,
                                onDismissRequest = { expandedStates["lugarSalon"] = false },
                                modifier = Modifier
                                    .width(
                                        with(LocalDensity.current) {
                                            (textfieldSizes["lugarSalon"]!!.width * 0.9f).toDp()
                                        }
                                    )
                                    .align(Alignment.Start) // Agrega este modificador
                            ) {
                                suggestionsLugarSalon.forEach { label ->
                                    DropdownMenuItem(
                                        text = { Text(text = label) },
                                        onClick = {
                                            viewModel.onValueLugar(label, "lugar")
                                            expandedStates["lugarSalon"] = false
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if (dataLugar.tipo == "Laboratorio") {
                            OutlinedTextField(
                                value = dataLugar.lugar,
                                onValueChange = { viewModel.onValueLugar(it, "lugar") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onGloballyPositioned { coordinates ->
                                        textfieldSizes["lugarLaboratorio"] =
                                            coordinates.size.toSize()
                                    },
                                label = { Text("Lugar") },
                                trailingIcon = {
                                    Icon(
                                        iconLugarLaboratorio,
                                        "contentDescription",
                                        Modifier.clickable {
                                            expandedStates["lugarLaboratorio"] =
                                                !expandedStates["lugarLaboratorio"]!!
                                        }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expandedStates["lugarLaboratorio"]!!,
                                onDismissRequest = { expandedStates["lugarLaboratorio"] = false },
                                modifier = Modifier
                                    .width(
                                        with(LocalDensity.current) {
                                            (textfieldSizes["lugarLaboratorio"]!!.width * 0.9f).toDp()
                                        }
                                    )
                                    .align(Alignment.Start) // Agrega este modificador
                            ) {
                                suggestionsLugarLaboratorio.forEach { label ->
                                    DropdownMenuItem(
                                        text = { Text(text = label) },
                                        onClick = {
                                            viewModel.onValueLugar(label, "lugar")
                                            expandedStates["lugarLaboratorio"] = false
                                        }
                                    )
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
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Button
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("lista_mi_qr")
                        }
                    ) {
                        Text("Generar QR")
                    }


                    // Botón para registrar QR
                    Button(onClick = {
                        when (tipoQR) {
                            "Asistencia" -> viewModel.registrarQrAsistencia()
                            "Lugar" -> viewModel.registrarQrLugar()
                            else -> {
                                // Manejo de error o acción alternativa
                            }
                        }
                    }) {
                        Text("Registrar QR")
                    }

                    // Mostrar CircularProgressIndicator si está cargando
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // Navegar a lista_mi_qr cuando se registre exitosamente
                    /* LaunchedEffect(viewModel.isLoading) {
                         if (!isLoading) {
                             navController.navigate("lista_mi_qr") {
                                 popUpTo("generate_qr_view") { inclusive = true }
                             }
                         }
                     }*/

                }
            }
        }
    )
}