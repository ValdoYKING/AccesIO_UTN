package com.pixelfusion.accesio_utn.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pixelfusion.accesio_utn.components.SplashScreen
import com.pixelfusion.accesio_utn.components.StartScreen
import com.pixelfusion.accesio_utn.view.AboutView
import com.pixelfusion.accesio_utn.view.CredentialView
import com.pixelfusion.accesio_utn.view.FormRegisterView
import com.pixelfusion.accesio_utn.view.HomeUserView
import com.pixelfusion.accesio_utn.view.HorarioView
import com.pixelfusion.accesio_utn.view.ImageCamView
import com.pixelfusion.accesio_utn.view.ImageUserView
import com.pixelfusion.accesio_utn.view.LegalScreen
import com.pixelfusion.accesio_utn.view.LoginScreen
import com.pixelfusion.accesio_utn.view.PerfilView
import com.pixelfusion.accesio_utn.view.RegisterScreen
import com.pixelfusion.accesio_utn.viewmodel.CredentialViewModel
import com.pixelfusion.accesio_utn.viewmodel.FormRegisterViewModel
import com.pixelfusion.accesio_utn.viewmodel.HomeViewModel
import com.pixelfusion.accesio_utn.viewmodel.LoginViewModel

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
    NavHost(navController, startDestination = "start_screen") {
        composable("start_screen") { StartScreen(navController) }
        composable("register_screen") { RegisterScreen(navController) }
        composable("login_screen") {
            val viewModelUL: LoginViewModel = viewModel();
            LoginScreen(navController, viewModelUL) }
        composable("legal_screen") { LegalScreen(navController) }
        composable("image_cam_view") { ImageCamView(navController) }
        composable("form_register_view") {
            val viewModelU: FormRegisterViewModel = viewModel()
            FormRegisterView(navController, viewModelU) }
        composable("image_user_view") { ImageUserView(navController) }
        composable("home_user_view"){
            val viewModelHome : HomeViewModel = viewModel()
            HomeUserView(navController,viewModelHome)
        }
        composable("credential_view") {
            val viewModelC: CredentialViewModel = viewModel()
            CredentialView(navController,viewModelC)
        }
        composable("horario_view"){
            HorarioView(navController)
        }
        composable("profile_view"){
            PerfilView(navController)
        }
        composable("about_view"){
            AboutView(navController)
        }
    }
}