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
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val state by viewModelU.stateHome.collectAsState() // Asegúrate de usar StateFlow para observar cambios
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        viewModelU.fetchData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent3(navController, currentRoute)
        },
        content = {
            Scaffold(
                topBar = {
                    com.pixelfusion.accesio_utn.components.ContenidoSuperiorWithTitle(
                        drawerState,
                        scope,
                        navController,
                        "Mi Perfil"
                    )
                },
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val painter = rememberImagePainter(
                            data = state.image_path,
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.placeholder)
                                error(R.drawable.error)
                            }
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .clickable {
                                    imageUri?.let { uri ->
                                        viewModelU.updateProfileImage(uri, navController)
                                    }
                                },
                            contentScale = ContentScale.Crop
                        )

                        Button(onClick = { navController.navigate("edit_image_user_view") }) {
                            Text("Tomar Imagen")
                        }

                        // Componente para seleccionar una nueva imagen
                        ImagePicker { uri ->
                            imageUri = uri
                            viewModelU.updateProfileImage(uri, navController)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    ProfileDetailItem(
                        label = "Nombre",
                        value = "${state.nombre} ${state.apellido}"
                    )
                    ProfileDetailItem(label = "Matrícula", value = state.matricula)
                    ProfileDetailItem(
                        label = "Correo Electrónico",
                        value = state.correo_electronico
                    )
                    ProfileDetailItem(label = "Teléfono", value = state.telefono)
                    ProfileDetailItem(label = "Carrera", value = state.carrera)
                    ProfileDetailItem(label = "Rol", value = state.id_rol)
                    ProfileDetailItem(
                        label = "Fecha de Nacimiento",
                        value = state.fecha_nacimiento
                    )
                    ProfileDetailItem(
                        label = "Número de Seguro Social",
                        value = state.num_seguro_social
                    )

                    Spacer(modifier = Modifier.height(20.dp))

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
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black
        )
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
