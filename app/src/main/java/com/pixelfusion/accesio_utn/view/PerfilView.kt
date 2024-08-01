import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.DrawerContent3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PerfilView(
    navController: NavController,
    viewModelU: UserProfileViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        viewModelU.fetchData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, "perfil_view")
        }
    ) {
        Scaffold(
            topBar = {
                ContenidoSuperiorWithTitle(
                    drawerState = drawerState,
                    scope = scope,
                    navController = navController,
                    title = "Perfil"
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                // Imagen de perfil
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    val painter = rememberImagePainter(
                        data = viewModelU.stateHome.image_path,
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.placeholder)
                            error(R.drawable.error)
                        }
                    )

                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .clickable {
                                // Accion para seleccionar una nueva imagen
                                // Llamar al ImagePicker cuando se haga clic en la imagen
                                imageUri?.let { uri ->
                                    viewModelU.updateProfileImage(uri, navController)
                                }
                            },
                        contentScale = ContentScale.Crop
                    )

                    // Componente para seleccionar una nueva imagen
                    ImagePicker { uri ->
                        imageUri = uri
                        viewModelU.updateProfileImage(uri, navController)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                ProfileDetailItem(label = "NOMBRE", value = "${viewModelU.stateHome.nombre} ${viewModelU.stateHome.apellido}")
                ProfileDetailItem(label = "MATRÍCULA", value = viewModelU.stateHome.matricula)
                ProfileDetailItem(label = "CORREO ELECTRÓNICO", value = viewModelU.stateHome.correo_electronico)
                ProfileDetailItem(label = "TELÉFONO", value = viewModelU.stateHome.telefono)
                ProfileDetailItem(label = "CARRERA", value = viewModelU.stateHome.carrera)
                ProfileDetailItem(label = "ROL", value = viewModelU.stateHome.id_rol)
                ProfileDetailItem(label = "FECHA DE NACIMIENTO", value = viewModelU.stateHome.fecha_nacimiento)
                ProfileDetailItem(label = "NÚMERO DE SEGURO SOCIAL", value = viewModelU.stateHome.num_seguro_social)

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para editar
                Button(
                    onClick = {
                        navController.navigate("editar_datos_view")
                    }
                ) {
                    Text("Editar")
                }
            }
        }
    }
}

@Composable
fun ImagePicker(
    onImageSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Seleccionar Imagen")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoSuperiorWithTitle(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    title: String
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        }
    )
}
@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$label:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}