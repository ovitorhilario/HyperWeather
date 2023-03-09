package com.hyper.hyperweather.data.repositories

import com.hyper.hyperweather.data.api.CityService
import com.hyper.hyperweather.data.model.CoordinatesModel
import com.hyper.hyperweather.data.model.WeatherModel

class CityRepositoryImpl(private val apiService: CityService) : CityRepository
{
    override suspend fun getCityByName(city: String, limit: Int, apiKey: String) : List<CoordinatesModel> {
        return apiService.getCityByName(city, limit, apiKey)
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String, units: String, lang: String) : WeatherModel {
        return apiService.getCurrentWeather(lat, lon, apiKey, units, lang)
    }
}