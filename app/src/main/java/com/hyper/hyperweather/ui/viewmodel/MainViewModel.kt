package com.hyper.hyperweather.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyper.hyperweather.data.model.WeatherModel
import com.hyper.hyperweather.data.repositories.CityRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository : CityRepositoryImpl) : ViewModel()
{
    private val _currentWeather = MutableLiveData<WeatherModel>()
    val currentWeatherState = MutableLiveData<WeatherState>()

    init {
        currentWeatherState.value = WeatherState.STOPPED
    }

    suspend fun getCityByName(city: String, limit: Int = 1) : Pair<Boolean, WeatherModel?> {

        var weather : WeatherModel? = null
        var isValid: Boolean = false

        try {
            setState(WeatherState.LOADING)

            val fetchJob = viewModelScope.launch(Dispatchers.IO) {
                val coords = repository.getCityByName(city, limit, apiKey)

                if(!coords.isNullOrEmpty()) {
                    weather = getCurrentWeather(coords[0].lat, coords[0].lon)
                    weather?.let { isValid = true }
                } else {
                    isValid = false
                }
            }

            fetchJob.join()
            setState(WeatherState.STOPPED)

            return if (isValid) {
                _currentWeather.value = weather!!
                true to weather
            } else {
                false to null
            }

        } catch (e: Exception) {
            setState(WeatherState.STOPPED)
            return false to null
        }
    }

    private suspend fun getCurrentWeather(lat: Double, lon: Double) : WeatherModel {
        return repository.getCurrentWeather(lat, lon, apiKey, units, lang)
    }

    sealed class WeatherState {
        object LOADING : WeatherState()
        object STOPPED : WeatherState()
    }

    private fun setState(state: WeatherState) {
        currentWeatherState.value = state
    }

    companion object {
        val apiKey : String = "YOUR_API_KEY"
        val units : String = "metric"
        val lang: String = "pt_br"
    }
}