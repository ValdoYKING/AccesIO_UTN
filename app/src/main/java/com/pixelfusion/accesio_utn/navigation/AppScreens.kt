package com.pixelfusion.accesio_utn.navigation

sealed class AppScreens(val route: String) {
    object SplashScreen : AppScreens("splash_screen")
    object MyApp : AppScreens("main_screen")
}