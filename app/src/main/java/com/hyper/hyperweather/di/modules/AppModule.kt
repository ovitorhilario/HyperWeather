package com.hyper.hyperweather.di.modules

import com.hyper.hyperweather.ui.viewmodel.MainViewModel
import com.hyper.hyperweather.data.api.CityService
import com.hyper.hyperweather.data.repositories.CityRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val CityServiceModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(CityService::class.java)
    }
}

val CityRepositoryModule = module {
    factory {
        CityRepositoryImpl(
            apiService = get()
        )
    }
}

val ViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}

