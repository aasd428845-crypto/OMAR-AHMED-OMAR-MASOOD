package com.wasal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.wasal.app.ui.navigation.WasalNavGraph
import com.wasal.app.ui.theme.WasalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WasalTheme {
                WasalNavGraph()
            }
        }
    }
}
