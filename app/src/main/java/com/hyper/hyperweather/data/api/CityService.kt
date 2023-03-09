package com.hyper.hyperweather.data.api

import com.hyper.hyperweather.data.model.CoordinatesModel
import com.hyper.hyperweather.data.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {

    @GET("/geo/1.0/direct")
    suspend fun getCityByName (
        @Query("q") city: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ) : List<CoordinatesModel>

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather (
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") lang: String
    ) : WeatherModel
}