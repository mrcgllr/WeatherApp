package com.example.weatherapp.api

import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {


    @GET("onecall?")
    suspend fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String,
        @Query("appid") key: String
    ): Response<WeatherResponse>

//    @GET("forecast?")
//    fun getForecast(
//        @Query("lat") latitude: String,
//        @Query("lon") longitude: String,
//        @Query("units") units: String
//    ): Response<ForecastResponse>
//
//    @GET("find?")
//    fun getCityDailyWeather(
//        @Query("lat") latitude: String,
//        @Query("lon") longitude: String,
//        @Query("cnt") cnt: String,
//        @Query("units") units: String
//    ): Response<CityDailyResponse>
}