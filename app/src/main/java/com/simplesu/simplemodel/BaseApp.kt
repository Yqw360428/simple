package com.simplesu.simplemodel

import android.app.Application
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.adjust.sdk.LogLevel
import kotlin.properties.Delegates

class BaseApp : Application() {
    companion object {
        var instance by Delegates.notNull<BaseApp>()
        var upUrl = ""
        var url = ""
        var isMainLaunch = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        Adjust.initSdk(
            AdjustConfig(
                this,
                "h1ythfa1hn28",
                AdjustConfig.ENVIRONMENT_PRODUCTION
            ).apply {
                setLogLevel(LogLevel.WARN)
            }
        )
    }
}