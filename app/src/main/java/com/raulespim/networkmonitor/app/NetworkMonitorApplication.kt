package com.raulespim.networkmonitor.app

import android.app.Application
import android.util.Log
import com.raulespim.networkmonitor.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NetworkMonitorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Send logs to your bug tracking system only in cases of an error or critical failure.
        if (priority == Log.ERROR || priority == Log.WARN) {
            // Send to a server or logging system
        }
    }
}