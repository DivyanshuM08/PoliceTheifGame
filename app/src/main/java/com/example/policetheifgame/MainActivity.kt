package com.example.policetheifgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.policetheifgame.ui.GameScreen
import com.example.policetheifgame.ui.theme.PoliceTheifGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PoliceTheifGameTheme {
                GameScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}