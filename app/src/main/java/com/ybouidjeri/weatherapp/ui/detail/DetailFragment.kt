package com.ybouidjeri.weatherapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import com.ybouidjeri.weatherapp.R
import com.ybouidjeri.weatherapp.Utils
import com.ybouidjeri.weatherapp.databinding.FragmentDetailBinding
import com.ybouidjeri.weatherapp.models.WeatherInfo
import com.ybouidjeri.weatherapp.ui.main.SharedViewModel
import java.util.*
import kotlin.math.roundToInt

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var weatherInfo: WeatherInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        sharedViewModel.selectedWeatherInfo.observe(this, object: Observer<WeatherInfo> {
            override fun onChanged(value: WeatherInfo) {
                weatherInfo = value
                val weatherResponse  = weatherInfo.info!!

                //set title
                val title = String.format(Locale.getDefault(), getString(R.string.detail_frag_title_pattern), weatherInfo.city.name) //Weather in ...
                val actionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
                actionBar?.title = title

                binding.tvCity.text = weatherInfo.city.name
                binding.tvDescription.text = weatherResponse.conditions[0].description.replaceFirstChar {it.uppercase()}
                binding.tvTemp.text = String.format(Locale.getDefault(), "%d°", weatherResponse.mainMeasure.temp.roundToInt())
                binding.tvTempFeel.text = String.format(Locale.getDefault(), "%d°", weatherResponse.mainMeasure.feels_like.roundToInt())
                binding.tvTempMinMax.text = String.format(Locale.getDefault(), "%d° / %d°",
                    weatherResponse.mainMeasure.temp_min.roundToInt(),
                    weatherResponse.mainMeasure.temp_max.roundToInt())

                binding.tvHumidity.text = String.format("%d%%", weatherResponse.mainMeasure.humidity.roundToInt())
                binding.tvPressure.text = String.format("%d hPa", weatherResponse.mainMeasure.pressure.roundToInt())
                binding.tvWind.text = String.format("%.1f m/sec", weatherResponse.wind.speed)
                binding.tvWindDir.text = Utils.getWindDirection(weatherResponse.wind.deg)
                binding.tvVisibility.text = String.format("%.1f km", weatherResponse.visibility / 1000.0)
                binding.tvClouds.text = String.format("%d %%", weatherResponse.clouds.all)

                val iconUrl = Utils.getIconUrlFromCode(weatherResponse.conditions[0].icon)
                Picasso.get()
                    .load(iconUrl)
                    .into(binding.ivConditionIcon)

                val sunriseDate = Utils.unixToDateObject(weatherResponse.sys.sunrise)
                val sunsetDate = Utils.unixToDateObject(weatherResponse.sys.sunset)
                binding.tvSunrise.setText(Utils.formatTime(sunriseDate))
                binding.tvSunset.setText(Utils.formatTime(sunsetDate))
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}