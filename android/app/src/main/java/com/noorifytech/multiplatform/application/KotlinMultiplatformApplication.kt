package com.noorifytech.multiplatform.application

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import org.jetbrains.anko.AnkoLogger

class KotlinMultiplatformApplication : Application(), AnkoLogger {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            println(throwable)
            throwable.printStackTrace()
            throwable?.cause?.printStackTrace()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}