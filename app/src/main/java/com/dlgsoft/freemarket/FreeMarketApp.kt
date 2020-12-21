package com.dlgsoft.freemarket

import android.app.Application
import com.dlgsoft.freemarket.di.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FreeMarketApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        startKoin {
            androidLogger()
            androidContext(this@FreeMarketApp)
            modules(
                networkModule,
                databaseModule,
                repositoryModule,
                viewModelModule,
                commonModule
            )
        }
    }
}
