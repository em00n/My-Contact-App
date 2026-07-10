package com.emon.mycontactapp

import androidx.compose.ui.window.ComposeUIViewController
import com.emon.mycontactapp.di.initKoin
import com.emon.mycontactapp.presentation.App
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * iOS entry point. The Swift side (iosApp) calls `MainViewControllerKt.MainViewController()` from a
 * SwiftUI `UIViewControllerRepresentable`. Koin + Napier are initialized once on first creation.
 */
fun MainViewController() = ComposeUIViewController(
    configure = {
        if (!isInitialized) {
            isInitialized = true
            Napier.base(DebugAntilog())
            initKoin()
        }
    }
) {
    App()
}

private var isInitialized = false
