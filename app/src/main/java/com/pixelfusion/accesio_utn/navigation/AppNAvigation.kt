package com.pixelfusion.accesio_utn.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.pixelfusion.accesio_utn.components.SplashScreen
import com.pixelfusion.accesio_utn.components.StartScreen
import com.pixelfusion.accesio_utn.helper.PreferenceHelper
import com.pixelfusion.accesio_utn.view.AboutView
import com.pixelfusion.accesio_utn.view.CredentialView
import com.pixelfusion.accesio_utn.view.FormRegisterView
import com.pixelfusion.accesio_utn.view.GenerateQrView
import com.pixelfusion.accesio_utn.view.HistoryUserView
import com.pixelfusion.accesio_utn.view.HomeUserView
import com.pixelfusion.accesio_utn.view.HorarioView
import com.pixelfusion.accesio_utn.view.ImageCamView
import com.pixelfusion.accesio_utn.view.ImageUserView
import com.pixelfusion.accesio_utn.view.LegalScreen
import com.pixelfusion.accesio_utn.view.LoginScreen
import com.pixelfusion.accesio_utn.view.PerfilView
import com.pixelfusion.accesio_utn.view.ScanQRAccessView
import com.pixelfusion.accesio_utn.view.ScanQRAssistView
import com.pixelfusion.accesio_utn.viewmodel.CredentialViewModel
import com.pixelfusion.accesio_utn.viewmodel.FormRegisterViewModel
import com.pixelfusion.accesio_utn.viewmodel.HomeViewModel
import com.pixelfusion.accesio_utn.viewmodel.ImageUserViewModel
import com.pixelfusion.accesio_utn.viewmodel.LoginViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScanQRAccessViewModel
import com.pixelfusion.accesio_utn.viewmodel.ScannerViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.route) {
        composable(AppScreens.SplashScreen.route) {
            SplashScreen(navController) // Pass navController to SplashScreen
        }
        composable(AppScreens.MyApp.route) {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val prefs = PreferenceHelper(context)
    val currentUser = FirebaseAuth.getInstance().currentUser
    // Determinar la pantalla de inicio
    val startDestination = when {
        currentUser?.email.isNullOrEmpty() && !prefs.hasSeenStartScreen -> "start_screen" // No autenticado y no ha visto la pantalla de inicio
        currentUser?.email.isNullOrEmpty() -> "login_screen" // No autenticado pero ha visto la pantalla de inicio
        else -> "home_user_view" // Autenticado
    }

    NavHost(navController, startDestination = startDestination) {
        composable("start_screen") {
            StartScreen(navController, context)
        }

        composable("form_register_view") {
            val viewModelU: FormRegisterViewModel = viewModel()
            FormRegisterView(navController, viewModelU)
        }

        composable("login_screen") {
            val viewModelUL: LoginViewModel = viewModel()
            LoginScreen(navController, viewModelUL)
        }

        composable("legal_screen") {
            LegalScreen(navController, LocalContext.current)
        }

        composable("image_cam_view") {
            val viewModel: ScannerViewModel = viewModel()
            ImageCamView(navController, viewModel)
        }

        composable("image_user_view") {
            val viewModel: ImageUserViewModel = viewModel()
            ImageUserView(navController, viewModel)
        }

        composable("home_user_view") {
            val viewModelHome: HomeViewModel = viewModel()
            HomeUserView(navController, viewModelHome)
        }

        composable("credential_view") {
            val viewModelC: CredentialViewModel = viewModel()
            CredentialView(navController, viewModelC)
        }

        composable("horario_view") {
            HorarioView(navController)
        }

        composable("profile_view") {
            PerfilView(navController)
        }

        composable("about_view") {
            AboutView(navController)
        }

        composable("scan_qr_access_view") {
            val viewModelScanQR: ScanQRAccessViewModel = viewModel()
            ScanQRAccessView(navController, viewModelScanQR)
        }

        composable("scan_qr_assist_view") {
            ScanQRAssistView(navController)
        }

        composable("generate_qr_view") {
            GenerateQrView(navController)
        }

        composable("history_user_view") {
            HistoryUserView(navController)
        }
    }
}
