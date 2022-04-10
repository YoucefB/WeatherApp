package com.ybouidjeri.newsapi.data

import com.ybouidjeri.weatherapp.models.WeatherResponse
import io.reactivex.Observable

interface WeatherRepository {

    fun getCurrentWeather(lat: Double, lon: Double): Observable<WeatherResponse>

}