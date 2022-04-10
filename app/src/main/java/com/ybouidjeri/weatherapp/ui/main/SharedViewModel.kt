package com.ybouidjeri.weatherapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ybouidjeri.weatherapp.models.WeatherInfo

class SharedViewModel : ViewModel() {

    val selectedWeatherInfo = MutableLiveData<WeatherInfo>()

    fun setSelectetItem(weatherInfo: WeatherInfo) {
        selectedWeatherInfo.value = weatherInfo
    }
}