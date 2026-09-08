package com.example.inventaryws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.inventaryws.core.navigation.AppNavHost
import com.example.inventaryws.core.ui.theme.InventaryWSTheme
import com.example.inventaryws.detail.ui.DetailScreen
import com.example.inventaryws.home.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventaryWSTheme {
                AppNavHost()
            }
        }
    }
}
