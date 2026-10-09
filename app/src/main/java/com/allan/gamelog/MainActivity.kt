package com.allan.gamelog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.allan.gamelog.ui.navigation.GameLogApp
import com.allan.gamelog.ui.theme.GamelogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamelogTheme {
                GameLogApp()
            }
        }
    }
}
