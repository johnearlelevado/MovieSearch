package com.example.challenge

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // set facebook stetho logging tool
        Stetho.initializeWithDefaults(this)
    }

}