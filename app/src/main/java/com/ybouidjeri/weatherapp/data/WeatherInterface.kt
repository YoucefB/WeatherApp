package com.ybouidjeri.bbcnews.data

import com.ybouidjeri.weatherapp.models.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    @GET("weather")
    fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): Observable<WeatherResponse>

}