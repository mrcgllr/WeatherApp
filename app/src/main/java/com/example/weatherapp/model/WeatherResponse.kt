package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: Current?=null,
    @SerializedName("daily")
    val daily: List<Daily>?=null,
    @SerializedName("lat")
    val lat: Double=0.0,
    @SerializedName("lon")
    val lon: Double=0.0,
    @SerializedName("timezone")
    val timezone: String="",
    @SerializedName("timezone_offset")
    val timezone_offset: Int=0
)