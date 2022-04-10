package com.ybouidjeri.newsapi.data

import com.ybouidjeri.bbcnews.data.WeatherInterface
import com.ybouidjeri.weatherapp.models.WeatherResponse
import io.reactivex.Observable

class ApiRepository(private val api: WeatherInterface): WeatherRepository {

    override fun getCurrentWeather(lat: Double, lon: Double): Observable<WeatherResponse> {
        return api.getCurrentWeather(lat, lon)
    }

}