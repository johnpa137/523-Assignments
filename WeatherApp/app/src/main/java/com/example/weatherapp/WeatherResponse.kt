package com.example.weatherapp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val sys: Sys
)

@Serializable
data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
data class Main(
    val temp: Double,
    @SerialName("temp_min") val tempMin: Double,
    @SerialName("temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

@Serializable
data class Wind(
    val speed: Double
)

@Serializable
data class Sys(
    val sunrise: Long,
    val sunset: Long
)
