package com.pixelfusion.accesio_utn.model

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

data class MenuButtonss(
    val text: String,
    val onClick: () -> Unit,
    val content: @Composable () -> Unit
)

data class ButtonData(val route: String, val text: String, val navController: NavController) {
    fun onClick() {
        navController.navigate(route)
    }
}