package com.example.travelapp.data.remote.dto.weather

import kotlinx.serialization.Serializable



@Serializable
data class WeatherResponse(
    val success: Boolean,
    val data:CurrentWeather
)



@Serializable
data class CurrentWeather(
    val temperature:Double,
    val weathercode:Int,
    val is_day:Int,
    val windspeed:Double
)