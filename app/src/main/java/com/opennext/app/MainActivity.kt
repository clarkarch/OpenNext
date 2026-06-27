package com.opennext.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.opennext.app.ui.navigation.OpenNextNavHost
import com.opennext.app.ui.theme.OpenNextTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenNextTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OpenNextNavHost()
                }
            }
        }
    }
}
