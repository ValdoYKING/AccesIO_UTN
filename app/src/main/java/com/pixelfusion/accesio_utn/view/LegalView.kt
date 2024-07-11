package com.pixelfusion.accesio_utn.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.pixelfusion.accesio_utn.R
import com.pixelfusion.accesio_utn.components.ButtonNext


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LegalScreen(navController: NavController, context: Context) {
    val sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    var isAccepted by remember {
        mutableStateOf(
            sharedPreferences.getBoolean(
                "terms_accepted",
                false
            )
        )
    }

    if (isAccepted) {
        // Navigate to the next screen if terms are already accepted
        //navController.navigate("login_screen")
        navController.navigate("image_user_view")
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Última actualización: 10 Jul 2024",
                            fontSize = 16.sp
                        )
                    },
                    modifier = Modifier.background(Color(0xFF1E88E5))
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.utnfoto), // Reemplaza con tu logo
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(
                        text = "Aviso de privacidad",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(termsAndConditions) { item ->
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Button(
                        onClick = {
                            editor.putBoolean("terms_accepted", true)
                            editor.apply()
                            isAccepted = true
                            //navController.navigate("login_screen")
                            navController.navigate("image_user_view")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Aceptar", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

val termsAndConditions = listOf(
    createAnnotatedString("En AcceciOUTN, nos comprometemos a proteger su privacidad y garantizar la seguridad de sus datos personales. Este Aviso de Privacidad describe cómo recopilamos, utilizamos, compartimos y protegemos la información que nos proporciona a través de nuestra aplicación móvil."),
    createAnnotatedString("1. Responsable del Tratamiento de Datos Personales", true),
    createAnnotatedString("AcceciOUTN es responsable del tratamiento de sus datos personales. Si tiene alguna pregunta o inquietud sobre este Aviso de Privacidad o sobre el tratamiento de sus datos personales, puede contactarnos en:"),
    createAnnotatedString("• Correo electrónico: utncredentials@gmail.com"),
    createAnnotatedString("• Teléfono: 55 5441 3182"),
    createAnnotatedString("2. Datos Personales Recopilados", true),
    createAnnotatedString("Recopilamos los siguientes datos personales de los estudiantes:"),
    createAnnotatedString("• Nombre y apellidos: Identificación personal"),
    createAnnotatedString("• Fecha de nacimiento: Verificación de edad"),
    createAnnotatedString("• Matrícula: Identificación académica"),
    createAnnotatedString("• Cuatrimestre: Información académica"),
    createAnnotatedString("• Número de Seguro Social (NSS): Identificación de seguridad social"),
    createAnnotatedString("• Correo electrónico: Comunicación y notificaciones"),
    createAnnotatedString("• Contraseña: Autenticación de usuario"),
    createAnnotatedString("• Número de teléfono: Comunicación y verificación"),
    createAnnotatedString("• Fotografía: Identificación visual"),
    createAnnotatedString("• Credencial digital: Acceso a servicios y funcionalidades"),
    createAnnotatedString("• Información de registro de entrada y salida: Control de acceso (fecha, hora de entrada, hora de salida, comentarios)"),
    createAnnotatedString("3. Finalidades del Tratamiento de Datos", true),
    createAnnotatedString("Utilizamos sus datos personales para las siguientes finalidades:"),
    createAnnotatedString("• Verificación de identidad y autenticación de usuarios: Asegurar que solo personas autorizadas accedan a la aplicación y sus funcionalidades."),
    createAnnotatedString("• Gestión y emisión de credenciales digitales: Proveer una alternativa digital segura a las credenciales físicas."),
    createAnnotatedString("• Registro y control de entrada y salida: Monitorear el acceso a instalaciones y eventos."),
    createAnnotatedString("• Comunicación con los usuarios sobre actualizaciones y avisos importantes: Mantener informados a los usuarios sobre cambios, actualizaciones y notificaciones relevantes."),
    createAnnotatedString("• Mejorar la seguridad y la eficiencia del acceso a las instalaciones: Optimizar el proceso de control de acceso."),
    createAnnotatedString("4. Compartición de Datos", true),
    createAnnotatedString("No compartimos sus datos personales con terceros, salvo en los siguientes casos:"),
    createAnnotatedString("• Obligaciones legales: Cuando sea necesario para cumplir con obligaciones legales y requerimientos gubernamentales."),
    createAnnotatedString("• Proveedores de servicios: Con proveedores que nos ayudan a operar y mejorar nuestra aplicación, bajo estrictas medidas de confidencialidad y seguridad"),
    createAnnotatedString("**5. Almacenamiento y Seguridad de los Datos**", true),
    createAnnotatedString("Sus datos personales se almacenan de manera segura en nuestros servidores. Implementamos las siguientes medidas de seguridad:"),
    createAnnotatedString("• Cifrado de datos: Todos los datos personales son cifrados durante la transmisión y el almacenamiento."),
    createAnnotatedString("• Autenticación multifactorial: Se requiere para el acceso a datos sensibles."),
    createAnnotatedString("• Monitoreo de seguridad: Se realiza un monitoreo constante de nuestros sistemas para detectar y prevenir accesos no autorizados."),
    createAnnotatedString("6. Derechos de los Usuarios", true),
    createAnnotatedString("Usted tiene derecho a:"),
    createAnnotatedString("• Acceso: Solicitar acceso a sus datos personales que poseemos."),
    createAnnotatedString("• Rectificación: Solicitar la corrección de datos inexactos o incompletos."),
    createAnnotatedString("• Cancelación: Solicitar la eliminación de sus datos personales cuando ya no sean necesarios para las finalidades establecidas."),
    createAnnotatedString("• Oposición: Oponerse al tratamiento de sus datos personales por motivos legítimos."),
    createAnnotatedString("Para ejercer sus derechos, puede contactarnos en utncredentials@gmail.com."),
    createAnnotatedString("7. Cambios al Aviso de Privacidad", true),
    createAnnotatedString("Nos reservamos el derecho de actualizar este Aviso de Privacidad en cualquier momento. Le notificaremos sobre cualquier cambio significativo a través de nuestra aplicación o por otros medios de comunicación.")
)

fun createAnnotatedString(text: String, isBold: Boolean = false): AnnotatedString {
    return buildAnnotatedString {
        if (isBold) {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(text)
            }
        } else {
            append(text)
        }
    }
}

