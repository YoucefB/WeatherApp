package com.ybouidjeri.weatherapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ybouidjeri.weatherapp.R
import com.ybouidjeri.weatherapp.Utils
import com.ybouidjeri.weatherapp.databinding.WeatherInfoListItemBinding
import com.ybouidjeri.weatherapp.models.WeatherInfo


import java.lang.ref.WeakReference
import kotlin.math.roundToInt


class WeatherInfoAdapter() : RecyclerView.Adapter<WeatherInfoAdapter.WeatherInfoViewHolder>() {

    private lateinit var weatherInfoList: List<WeatherInfo>
    private lateinit var listener: ClickListener

    constructor(weatherInfoList: List<WeatherInfo>, listener: ClickListener) : this() {
        this.weatherInfoList = weatherInfoList
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherInfoViewHolder {
        val context = parent.context
        val itemView : View = LayoutInflater.from(context)
            .inflate(R.layout.weather_info_list_item, parent, false)

        return WeatherInfoViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(holder: WeatherInfoViewHolder, position: Int) {
        val weatherInfo : WeatherInfo = weatherInfoList[position]
        holder.bind(weatherInfo)
    }

    override fun getItemCount(): Int {
        return weatherInfoList.size
    }

    inner class WeatherInfoViewHolder(itemView: View, listener: ClickListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val binding = WeatherInfoListItemBinding.bind(itemView)
        private val listenerRef: WeakReference<ClickListener> = WeakReference(listener)

        init {
            itemView.setOnClickListener(this)
        }

        //"${weatherInfo.info!!.mainMeasure.temp.roundToInt()}°"

        fun bind(weatherInfo: WeatherInfo) {
            binding.tvCityName.text = weatherInfo.city.name
            if (weatherInfo.info != null) {
                binding.tvTemp.text = String.format("%d°", weatherInfo.info!!.mainMeasure.temp.roundToInt())
                binding.tvHumidity.text = String.format("%d %%", weatherInfo.info!!.mainMeasure.humidity.roundToInt())
                binding.tvDescription.text = weatherInfo.info!!.conditions[0].description.replaceFirstChar {it.uppercase()}
                val iconUrl = Utils.getIconUrlFromCode(weatherInfo.info!!.conditions[0].icon)
                binding.ivConditionIcon.visibility = View.VISIBLE
                Picasso.get()
                    .load(iconUrl)
                    .into(binding.ivConditionIcon)
            } else {
                binding.tvTemp.setText("")
                binding.tvHumidity.setText("")
                binding.tvDescription.setText("")
                binding.ivConditionIcon.visibility = View.INVISIBLE

            }
        }

        override fun onClick(view: View?) {
            val pos : Int = adapterPosition
            listenerRef.get()?.onPositionClicked(view!!, pos)
        }
    }

    interface ClickListener {
        fun onPositionClicked(view: View, position: Int)
    }
}