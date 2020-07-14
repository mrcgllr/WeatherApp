package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Daily(
    @SerializedName("clouds")
    val clouds: Int=0,
    @SerializedName("dew_point")
    val dew_point: Double=0.0,
    @SerializedName("dt")
    val dt: Int=0,
    @SerializedName("feels_like")
    val feels_like: FeelsLike?=null,
    @SerializedName("humidity")
    val humidity: Int=0,
    @SerializedName("pressure")
    val pressure: Int=0,
    @SerializedName("rain")
    val rain: Double=0.0,
    @SerializedName("sunrise")
    val sunrise: Int=0,
    @SerializedName("sunset")
    val sunset: Int=0,
    @SerializedName("temp")
    val temp: Temp?=null,
    @SerializedName("uvi")
    val uvi: Double=0.0,
    @SerializedName("weather")
    val weather: List<Weather>?=null,
    @SerializedName("wind_deg")
    val wind_deg: Int=0,
    @SerializedName("wind_speed")
    val wind_speed: Double=0.0
)