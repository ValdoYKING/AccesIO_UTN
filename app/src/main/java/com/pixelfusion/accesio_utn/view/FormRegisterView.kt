package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext
import com.pixelfusion.accesio_utn.viewmodel.FormRegisterViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScannerViewModel
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FormRegisterView(
    navController: NavController,
    viewModel: FormRegisterViewModel,
) {
    val context = LocalContext.current
    val dataU = viewModel.state
    var passwordVisible by remember { mutableStateOf(false) }
    val state = rememberDatePickerState()
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    val suggestions = listOf(
        "PERSONAL",
        "ADMINISTRATIVO",
        "PROFESOR",
        "VISITA",
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
        "LIC EN INNOVACIÓN DE NEGOCIOS Y MERCADOTECNIA",
        "MAESTRÍA EN GESTIÓN E INNOVACIÓN DE LAS ORGANIZACIONES",
        "MAESTRÍA EN SISTEMAS DE GESTIÓN AMBIENTAL",
        "MAESTRÍA EN INNOVACIÓN Y NEGOCIOS"
    )
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopBarRegister()
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.nombre,
                    onValueChange = { viewModel.onValue(it, "nombre") },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clip(RoundedCornerShape(18.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.apellido,
                    onValueChange = { viewModel.onValue(it, "apellido") },
                    label = { Text("Apellidos") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clip(RoundedCornerShape(18.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                if (viewModel.showDialog) {
                    DatePickerDialog(
                        onDismissRequest = { viewModel.closeDialog() },
                        confirmButton = {
                            Button(onClick = { viewModel.closeDialog() }) {
                                Text(text = "Confirmar")
                            }
                        },
                        dismissButton = {
                            OutlinedButton(onClick = { viewModel.closeDialog() }) {
                                Text(text = "Cancelar")
                            }
                        },
                        modifier = Modifier,
                        shape = DatePickerDefaults.shape,
                        properties = DialogProperties(usePlatformDefaultWidth = true)
                    ) {
                        val date = state.selectedDateMillis
                        date?.let {
                            val localDate =
                                Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                            val fechaFormateada =
                                "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
                            viewModel.onValue(fechaFormateada, "fecha_nacimiento")
                        }
                        DatePicker(state = state, modifier = Modifier.padding(8.dp))
                    }
                }
                OutlinedTextField(
                    value = dataU.fecha_nacimiento,
                    onValueChange = { viewModel.onValue(it, "fecha_nacimiento") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "Fecha de nacimiento",
                            //fontSize = 18.sp,
                            //fontWeight = FontWeight.Bold
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                                .clickable { viewModel.openDialog() }
                        )
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.correo_electronico,
                    onValueChange = { viewModel.onValue(it, "correo_electronico") },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.matricula,
                    onValueChange = { viewModel.onValue(it, "matricula") },
                    label = { Text("Matrícula") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            /*item {
                OutlinedTextField(
                    value = dataU.carrera,
                    onValueChange = { viewModel.onValue(it, "carrera") },
                    label = { Text("Carrera") },
                    modifier = Modifier
                        .fillMaxWidth(),
                        //.clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }*/
            item {
                OutlinedTextField(
                    value = dataU.carrera,
                    onValueChange = { viewModel.onValue(it, "carrera") },
                    modifier = Modifier
                        //.padding(horizontal = 12.dp)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textfieldSize = coordinates.size.toSize()
                        },
                    label = {
                        Text(
                            text = "Cargo o carrera"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    trailingIcon = {
                        Icon(icon, "contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    /*modifier = Modifier
                        .width(with(LocalDensity.current){textfieldSize.width.toDp()})*/
                    modifier = Modifier
                        .width(with(LocalDensity.current) { (textfieldSize.width * 0.9f).toDp() })
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(
                            text = { Text(text = label) },
                            onClick = {
                                viewModel.onValue(label, "carrera")
                                expanded = false
                            })
                    }
                }
            }
            item {
                if (dataU.carrera.isNotEmpty() && dataU.carrera != "PERSONAL" && dataU.carrera != "ADMINISTRATIVO" && dataU.carrera != "DOCENTE" && dataU.carrera != "VISITA") {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = dataU.cuatrimestre,
                        onValueChange = { viewModel.onValue(it, "cuatrimestre") },
                        label = { Text("Cuatrimestre") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        //.clip(RoundedCornerShape(8.dp)),
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        })
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (dataU.carrera.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            //item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.num_seguro_social,
                    onValueChange = { viewModel.onValue(it, "num_seguro_social") },
                    label = { Text("Número seguro social") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.clip(RoundedCornerShape(8.dp)),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.telefono,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 1,
                    onValueChange = { viewModel.onValue(it, "telefono") },
                    label = {
                        Text(
                            text = "Telefono",
                            //fontSize = 18.sp,
                            //fontWeight = FontWeight.Bold
                        )
                    },
                    //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })

                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OutlinedTextField(
                    value = dataU.contrasena,
                    onValueChange = { viewModel.onValue(it, "contrasena") },
                    label = { Text("Contraseña") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { Spacer(modifier = Modifier.height(30.dp)) }
            item {
                Button(
                    onClick = {
                        if (dataU.nombre.isEmpty() || dataU.apellido.isEmpty() || dataU.matricula.isEmpty() || dataU.carrera.isEmpty() || dataU.correo_electronico.isEmpty()
                            || dataU.fecha_nacimiento.isEmpty() || dataU.contrasena.isEmpty()
                        ) {
                            Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.registerUser(navController, context)
                        }
                    }
                ) {
                    Text("Enviar")
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ya tengo cuenta",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate("login_screen")
                    }
                )
            }
            /*item{
                ButtonNext(navController, "login_screen")
            }*/
        }
    }
}

@Composable
fun TopBarRegister(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF0D9462),
        border = BorderStroke(2.dp, Color(0xFF0D9462)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.utnfoto), // reemplaza con tu logo
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registrate",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


private val Icons.Filled.Visibility: ImageVector
    get() {
        if (_visibility != null) {
            return _visibility!!
        }
        _visibility =
            materialIcon(name = "Filled.Visibility") {
                materialPath {
                    moveTo(12.0f, 4.5f)
                    curveTo(7.0f, 4.5f, 2.73f, 7.61f, 1.0f, 12.0f)
                    curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
                    reflectiveCurveToRelative(9.27f, -3.11f, 11.0f, -7.5f)
                    curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
                    close()
                    moveTo(12.0f, 17.0f)
                    curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
                    reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
                    reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
                    reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
                    close()
                    moveTo(12.0f, 9.0f)
                    curveToRelative(-1.66f, 0.0f, -3.0f, 1.34f, -3.0f, 3.0f)
                    reflectiveCurveToRelative(1.34f, 3.0f, 3.0f, 3.0f)
                    reflectiveCurveToRelative(3.0f, -1.34f, 3.0f, -3.0f)
                    reflectiveCurveToRelative(-1.34f, -3.0f, -3.0f, -3.0f)
                    close()
                }
            }
        return _visibility!!
    }

private var _visibility: ImageVector? = null

private val Icons.Filled.VisibilityOff: ImageVector
    get() {
        if (_visibilityOff != null) {
            return _visibilityOff!!
        }
        _visibilityOff =
            materialIcon(name = "Filled.VisibilityOff") {
                materialPath {
                    moveTo(12.0f, 7.0f)
                    curveToRelative(2.76f, 0.0f, 5.0f, 2.24f, 5.0f, 5.0f)
                    curveToRelative(0.0f, 0.65f, -0.13f, 1.26f, -0.36f, 1.83f)
                    lineToRelative(2.92f, 2.92f)
                    curveToRelative(1.51f, -1.26f, 2.7f, -2.89f, 3.43f, -4.75f)
                    curveToRelative(-1.73f, -4.39f, -6.0f, -7.5f, -11.0f, -7.5f)
                    curveToRelative(-1.4f, 0.0f, -2.74f, 0.25f, -3.98f, 0.7f)
                    lineToRelative(2.16f, 2.16f)
                    curveTo(10.74f, 7.13f, 11.35f, 7.0f, 12.0f, 7.0f)
                    close()
                    moveTo(2.0f, 4.27f)
                    lineToRelative(2.28f, 2.28f)
                    lineToRelative(0.46f, 0.46f)
                    curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1.0f, 12.0f)
                    curveToRelative(1.73f, 4.39f, 6.0f, 7.5f, 11.0f, 7.5f)
                    curveToRelative(1.55f, 0.0f, 3.03f, -0.3f, 4.38f, -0.84f)
                    lineToRelative(0.42f, 0.42f)
                    lineTo(19.73f, 22.0f)
                    lineTo(21.0f, 20.73f)
                    lineTo(3.27f, 3.0f)
                    lineTo(2.0f, 4.27f)
                    close()
                    moveTo(7.53f, 9.8f)
                    lineToRelative(1.55f, 1.55f)
                    curveToRelative(-0.05f, 0.21f, -0.08f, 0.43f, -0.08f, 0.65f)
                    curveToRelative(0.0f, 1.66f, 1.34f, 3.0f, 3.0f, 3.0f)
                    curveToRelative(0.22f, 0.0f, 0.44f, -0.03f, 0.65f, -0.08f)
                    lineToRelative(1.55f, 1.55f)
                    curveToRelative(-0.67f, 0.33f, -1.41f, 0.53f, -2.2f, 0.53f)
                    curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
                    curveToRelative(0.0f, -0.79f, 0.2f, -1.53f, 0.53f, -2.2f)
                    close()
                    moveTo(11.84f, 9.02f)
                    lineToRelative(3.15f, 3.15f)
                    lineToRelative(0.02f, -0.16f)
                    curveToRelative(0.0f, -1.66f, -1.34f, -3.0f, -3.0f, -3.0f)
                    lineToRelative(-0.17f, 0.01f)
                    close()
                }
            }
        return _visibilityOff!!
    }
private var _visibilityOff: ImageVector? = null