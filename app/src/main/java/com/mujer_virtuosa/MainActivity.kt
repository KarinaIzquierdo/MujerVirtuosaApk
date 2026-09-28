package com.mujer_virtuosa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.mujer_virtuosa.ui.navigation.AppNavigation
import com.mujer_virtuosa.ui.theme.Mujer_virtuosaAppTheme
import com.mujer_virtuosa.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TokenManager.init(applicationContext)

        enableEdgeToEdge()
        setContent {
            Mujer_virtuosaAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}