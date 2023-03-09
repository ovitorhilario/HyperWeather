package com.hyper.hyperweather.application

import android.app.Application
import com.hyper.hyperweather.di.modules.CityRepositoryModule
import com.hyper.hyperweather.di.modules.CityServiceModule
import com.hyper.hyperweather.di.modules.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(CityServiceModule, CityRepositoryModule, ViewModelModule)
        }
    }
}