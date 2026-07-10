package com.emon.mycontactapp

import android.app.Application
import com.emon.mycontactapp.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext

class ContactListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin {
            androidContext(this@ContactListApp)
        }
    }
}
