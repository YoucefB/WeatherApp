package com.ybouidjeri.weatherapp.models

data class WeatherInfo(
    val city: City,
    var info: WeatherResponse?
)