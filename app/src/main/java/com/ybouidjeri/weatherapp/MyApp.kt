package com.ybouidjeri.weatherapp

import androidx.multidex.MultiDexApplication
import com.ybouidjeri.bbcnews.data.WeatherInterface
import com.ybouidjeri.newsapi.data.ApiRepository
import com.ybouidjeri.newsapi.data.WeatherRepository
import com.ybouidjeri.weatherapp.data.ServiceBuilder
import java.util.*

class MyApp: MultiDexApplication() {

    var language  = Locale.getDefault().getLanguage().lowercase()

    @Override
    override fun onCreate() {
        super.onCreate()

        val acceptedLanguages = setOf("en", "fr")
        if (language !in acceptedLanguages) {
            language = "en"
        }
    }

    val weatherRepository : WeatherRepository
        get() = ApiRepository(ServiceBuilder.buildService(WeatherInterface::class.java, language))

}