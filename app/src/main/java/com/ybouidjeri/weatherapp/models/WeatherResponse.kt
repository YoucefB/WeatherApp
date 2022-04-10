package com.ybouidjeri.weatherapp.models

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("weather")
    val conditions: List<Condition>,
    @SerializedName("main")
    val mainMeasure: MainMeasure,
    val wind: Wind,
    val visibility: Int,
    val clouds: Clouds,
    val sys: Sys
)

data class Condition (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainMeasure(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity : Double,

    val sea_level: Double?,
    val grnd_level: Double?
)


data class Wind(
    val speed: Double,
    val deg: Double
)

data class Clouds(
    val all: Int
)

data class Sys (
    val sunrise: Long,
    val sunset: Long
)

