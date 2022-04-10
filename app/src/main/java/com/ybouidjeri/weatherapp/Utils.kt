package com.ybouidjeri.weatherapp

import com.ybouidjeri.weatherapp.models.City
import com.ybouidjeri.weatherapp.models.Location
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {
    const val API_KEY = "374faae1865a6eef77d25d173c4010d8"

    fun getListOfCities(): List<City> {

        val cities = mutableListOf<City>()

        cities.add(City("Lisbon", Location(38.722297, -9.139349)))
        cities.add(City("Madrid", Location(40.419979, -3.688726)))
        cities.add(City("Paris", Location(48.865581, 2.321433)))
        cities.add(City("Berlin", Location(52.517535, 13.376547)))
        cities.add(City("Copenhagen", Location(55.681132, 12.534148)))
        cities.add(City("Rome", Location(41.905912, 12.461244)))
        cities.add(City("London", Location(51.500863, -0.124627)))
        cities.add(City("Dublin", Location(53.339076, -6.272673)))
        cities.add(City("Prague", Location(50.087734, 14.420768)))
        cities.add(City("Vienna", Location(48.205800, 16.364880)))

        return cities
    }


    fun getIconUrlFromCode(code: String): String {
        return "http://openweathermap.org/img/wn/$code@2x.png"
    }

    fun formatDate(date: Date): String {
        val formater = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formater.format(date)
    }

    fun formatTime(date: Date) : String {
        val formater = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formater.format(date)
    }

    fun unixToDateObject(timestamp: Long) : Date{
        return Date(timestamp * 1000)
    }


    fun  toTextualDescription(degree: Double): String{
        if (degree>337.5) return "Northerly"
        if (degree>292.5) return "North Westerly"
        if(degree>247.5) return "Westerly"
        if(degree>202.5) return "South Westerly"
        if(degree>157.5) return "Southerly"
        if(degree>122.5) return "South Easterly"
        if(degree>67.5) return "Easterly"
        if(degree>22.5) return "North Easterly"
        return "Northerly"
    }

    fun getWindDirection(degree: Double) : String {
        val compassSector = arrayListOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S",
            "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N")

        val direction = compassSector[(degree / 22.5).roundToInt()]
        return direction
    }
}