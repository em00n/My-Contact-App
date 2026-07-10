package com.emon.mycontactapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.emon.mycontactapp.presentation.navigation.AppNavigation
import com.emon.mycontactapp.ui.theme.MyContactAppTheme
import com.emon.mycontactapp.ui.theme.White

/**
 * Shared application root. Both the Android `MainActivity` and the iOS `MainViewController` call
 * this, so the entire UI tree lives in commonMain. The Coil image loader is configured with the
 * Ktor network fetcher (backed by OkHttp on Android, Darwin on iOS).
 */
@Composable
fun App() {

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .crossfade(true)
            .build()
    }

    MyContactAppTheme {
        Scaffold(
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
