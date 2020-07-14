package com.example.weatherapp.repository

import com.example.weatherapp.api.RetrofitService

class CityWeatherRepository {


    suspend fun getWeather(
        latitude: String,
        longitude: String,
        units: String,
        key:String
    ) = RetrofitService.api.getWeather(latitude, longitude, units,key)
}