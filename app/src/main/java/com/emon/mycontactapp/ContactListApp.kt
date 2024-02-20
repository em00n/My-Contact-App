package com.emon.mycontactapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import de.hdodenhof.circleimageview.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class ContactListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}