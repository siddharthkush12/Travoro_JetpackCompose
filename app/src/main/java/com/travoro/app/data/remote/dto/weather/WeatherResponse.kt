package com.travoro.app.data.remote.dto.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val code: Int,
    val success: Boolean,
    val message: String,
    val data: WeatherData,
)

@Serializable
data class WeatherData(
    val location: String,
    val forecast: List<ForecastDay>,
    val alerts: List<WeatherAlert>,
)

@Serializable
data class ForecastDay(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val avgTemp: Double,
    val condition: String,
    val icon: String,
    val humidity: Int,
    val rainChance: Int,
    val windSpeed: Double,
    val uv: Double,
)

@Serializable
data class WeatherAlert(
    val headline: String,
    val severity: String,
    val urgency: String,
    val areas: String,
    val category: String,
    val effective: String,
    val expires: String,
    val description: String,
    val instruction: String?,
)

// UI MODEL DATA MODEL

data class WeatherUiModel(
    val city: String,
    val todayTemp: Double,
    val todayCondition: String,
    val todayIcon: String,
    val todayHumidity: Int,
    val todayWindSpeed: Double,
    val todayUv: Double,
    val forecast: List<ForecastUiModel>,
    val alerts: List<AlertUiModel>,
)

data class ForecastUiModel(
    val date: String,
    val avgTemp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val condition: String,
    val icon: String,
    val rainChance: Int,
    val uv: Double,
)

data class AlertUiModel(
    val headline: String,
    val severity: String,
    val urgency: String,
    val areas: String,
    val category: String,
    val effective: String,
    val expires: String,
    val description: String,
    val instruction: String?,
)
