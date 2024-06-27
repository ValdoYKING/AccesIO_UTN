package com.pixelfusion.accesio_utn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pixelfusion.accesio_utn.navigation.AppNavigation
import com.pixelfusion.accesio_utn.ui.theme.AccesIOUTNTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            AccesIOUTNTheme {
                //MyApp()
                AppNavigation()
            }
        }
    }
}
