package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.repository.CityWeatherRepository
import kotlinx.coroutines.launch

class CityWeatherViewModel : ViewModel() {

    private val repository = CityWeatherRepository()

    var weatherData = MutableLiveData<WeatherResponse>()


    fun getWeather(
        latitude: String,
        longitude: String,
        units: String,
        key: String
    ) = viewModelScope.launch {
        weatherData.value = repository.getWeather(latitude, longitude, units, key).body()
        
    }


}