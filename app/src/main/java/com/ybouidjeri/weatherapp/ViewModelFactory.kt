package com.ybouidjeri.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ybouidjeri.newsapi.data.WeatherRepository
import com.ybouidjeri.weatherapp.ui.list.ListViewModel

class ViewModelFactory constructor(private val weatherRepository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ListViewModel::class.java) -> ListViewModel(weatherRepository)
                //isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(weatherRepository)

                else ->
                    throw IllegalArgumentException("ViewModel class (${modelClass.name}) is not mapped")
            }
        } as T
}