package com.pixelfusion.accesio_utn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pixelfusion.accesio_utn.navigation.AppNavigation
import com.pixelfusion.accesio_utn.ui.theme.AccesIOUTNTheme
import com.pixelfusion.accesio_utn.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent { 
            AccesIOUTNTheme {
                //MyApp()
                AppNavigation()
                //FlipCard()
            }
        }
    }
}

@Composable
fun FlipCard() {
    var isFront by remember { mutableStateOf(true) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isFront) {
        rotation.animateTo(
            targetValue = if (isFront) 0f else 180f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { isFront = !isFront }
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        if (rotation.value <= 90f) {
            FrontCardContent()
        } else {
            BackCardContent()
        }
    }
}

@Composable
fun FrontCardContent() {
    Card(
        modifier = Modifier.fillMaxSize(),
        //backgroundColor = MaterialTheme.colors.primary
        //contentColor = Color.Red
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Front Side", style = MaterialTheme.typography.bodyLarge, color = Color.Red)
        }
    }
}

@Composable
fun BackCardContent() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
        //backgroundColor = MaterialTheme.colors.secondary
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text("Back Side", style = MaterialTheme.typography.titleLarge, color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    AccesIOUTNTheme {
        FlipCard()
    }
}