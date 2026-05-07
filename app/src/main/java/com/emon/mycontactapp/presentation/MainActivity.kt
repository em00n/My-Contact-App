package com.emon.mycontactapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.emon.mycontactapp.presentation.navigation.AppNavigation
import com.emon.mycontactapp.ui.theme.MyContactAppTheme
import com.emon.mycontactapp.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        // Configure splash screen behavior
        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            MyContactAppTheme {
                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    containerColor = White
                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}