package com.hyper.hyperweather.data.repositories

import com.hyper.hyperweather.data.model.CoordinatesModel
import com.hyper.hyperweather.data.model.WeatherModel

interface CityRepository {

    suspend fun getCityByName(city: String, limit: Int, apiKey: String) : List<CoordinatesModel>

    suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String, units: String, lang: String) : WeatherModel
}