package com.ybouidjeri.weatherapp.ui.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ybouidjeri.newsapi.data.WeatherRepository
import com.ybouidjeri.weatherapp.models.City
import com.ybouidjeri.weatherapp.models.WeatherResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ListViewModel (private val weatherRepository: WeatherRepository) : ViewModel() {

    companion object {
        const val TAG = "WEATHER_LIST_VM"
    }

    val apiResponseMLD: MutableLiveData<WeatherResponse> = MutableLiveData<WeatherResponse>()
    val apiResponseListMLD: MutableLiveData<List<WeatherResponse>> = MutableLiveData<List<WeatherResponse>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()

    fun getWeather(lat: Double, lon: Double) {
        val observable: Observable<WeatherResponse> = weatherRepository.getCurrentWeather(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        val observer: Observer<WeatherResponse> = object: Observer<WeatherResponse> {

            override fun onSubscribe(d: Disposable) {
                loading.value = true
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "Error: ${e.message}")
                loading.value = false
                error.value = true
                e.printStackTrace()
            }

            override fun onNext(weatherResponse: WeatherResponse) {
                Log.d(TAG, weatherResponse.toString())
                loading.value = false
                apiResponseMLD.value = weatherResponse
            }

            override fun onComplete() {
            }
        }

        observable.subscribe(observer)
    }



    fun getWeatherList(cities: List<City>) {

        val observablesList : MutableList<Observable<WeatherResponse>> = mutableListOf()
        for (city in cities) {
            val observable: Observable<WeatherResponse> = weatherRepository.getCurrentWeather(city.location!!.lat, city.location!!.lon)
            observablesList.add(observable)
        }

        val observer: Observer<MutableList<Array<Any>>> = object: Observer<MutableList<Array<Any>>> {  //SingleObserver

            override fun onSubscribe(d: Disposable) {
                loading.value = true
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "Error: ${e.message}")
                loading.value = false
                error.value = true
                e.printStackTrace()
            }

            override fun onComplete() {
            }

            override fun onNext(t: MutableList<Array<Any>>) {
                val weatherResponseList : MutableList<WeatherResponse> = mutableListOf()
                for (o in t[0]) {
                    val res = o as WeatherResponse
                    weatherResponseList.add(res)
                }
                loading.value = false
                apiResponseListMLD.value = weatherResponseList
            }
        }

        //------------------------------------------------------------------------------------------

        Observable.zip(observablesList) { args ->Arrays.asList(args)}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)

    }


}