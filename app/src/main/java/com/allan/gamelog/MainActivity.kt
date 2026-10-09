package com.allan.gamelog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.allan.gamelog.ui.screens.gamelist.GameListScreen
import com.allan.gamelog.ui.theme.GamelogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamelogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
