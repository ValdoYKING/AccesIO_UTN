package com.pixelfusion.accesio_utn.navigation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.pixelfusion.accesio_utn.MainActivity
import com.pixelfusion.accesio_utn.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    private val splashScreenDuration = 1200L // Duración de la splash screen en milisegundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usar el tema splash
        setTheme(R.style.Theme_AccesIOUTN_Splash)

        // Lanzar MainActivity después de un retraso
        lifecycleScope.launch {
            delay(splashScreenDuration)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}