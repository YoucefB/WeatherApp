package com.ybouidjeri.weatherapp.ui.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.location.*
import com.ybouidjeri.weatherapp.R
import com.ybouidjeri.weatherapp.Utils
import com.ybouidjeri.weatherapp.adapters.WeatherInfoAdapter
import com.ybouidjeri.weatherapp.databinding.FragmentListBinding
import com.ybouidjeri.weatherapp.models.City
import com.ybouidjeri.weatherapp.models.WeatherInfo
import com.ybouidjeri.weatherapp.models.WeatherResponse
import com.ybouidjeri.weatherapp.obtainViewModel
import com.ybouidjeri.weatherapp.ui.main.SharedViewModel

class ListFragment : Fragment() {

    companion object {
        const val TAG = "WEATHER_LIST_FRAG"
    }
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherAdapter: WeatherInfoAdapter
    private lateinit var listViewModel: ListViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var weatherInfoList: MutableList<WeatherInfo>

    private lateinit var requestLocationPermission : ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: com.ybouidjeri.weatherapp.models.Location? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val srl: SwipeRefreshLayout = binding.srl
        val rvWeatherInfo = binding.rvWeatherInfo

        listViewModel = obtainViewModel(ListViewModel::class.java)
        weatherInfoList = mutableListOf()

        val cities : MutableList<City> = mutableListOf()
        cities.addAll(Utils.getListOfCities())

        //fill cities
        val currentPlace = City(getString(R.string.lablel_current_location), null)
        val currentWeatherInfo = WeatherInfo(currentPlace,null)
        weatherInfoList.add(currentWeatherInfo)

        for (city in cities) {
            val weatherInfo = WeatherInfo(city, null)
            weatherInfoList.add(weatherInfo)
        }

        listViewModel.apiResponseMLD.observe(this, object : Observer<WeatherResponse> {
            override fun onChanged(weatherResponse: WeatherResponse?) {
                if (weatherResponse != null) {
                    weatherInfoList.get(0).info = weatherResponse
                    weatherAdapter.notifyItemChanged(0)
                }
            }
        })

        listViewModel.apiResponseListMLD.observe(this, object : Observer<List<WeatherResponse>> {
            override fun onChanged(weatherResponsesList: List<WeatherResponse>?) {
                //stop showing progressbar
                srl.isRefreshing = false

                if (weatherResponsesList != null) {
                    for (i in 1..weatherInfoList.size -1) { //skip the first elemement (current location)
                        val weatherInfo = weatherInfoList.get(i)
                        weatherInfo.info = weatherResponsesList.get(i-1)
                        Log.d(TAG, "${weatherInfo.city.name} ${weatherInfo.info!!.mainMeasure.temp}")
                    }
                    weatherAdapter.notifyDataSetChanged()
                }
            }
        })

        listViewModel.error.observe(this, object: Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    srl.isRefreshing = false
                    Toast.makeText(requireContext(), getString(R.string.error_loading), Toast.LENGTH_LONG).show()
                }
            }
        })

        //Swipe to Refresh
        srl.setOnRefreshListener {
            listViewModel.getWeatherList(cities)
            if (currentLocation != null) {
                listViewModel.getWeather(currentLocation!!.lat, currentLocation!!.lon)
            }
        }

        val clickListener = object : WeatherInfoAdapter.ClickListener {
            override fun onPositionClicked(view: View, position: Int) {
                //prevent app crash when updating and user click on list Item
                if (position < weatherInfoList.size) {
                    val selected = weatherInfoList.get(position)
                    sharedViewModel.setSelectetItem(selected)
                    findNavController().navigate(R.id.action_ListFragment_to_DetailFragment)
                }
            }
        }

        rvWeatherInfo.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        weatherAdapter = WeatherInfoAdapter(weatherInfoList, clickListener)
        rvWeatherInfo.setAdapter(weatherAdapter)

        listViewModel.getWeatherList(cities)

        //------------------------------------------------------------------------------------------
        requestLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted ) {
                getCurrentLocation()
            }
        }
        //------------------------------------------------------------------------------------------

        val locationPermission: String = Manifest.permission.ACCESS_FINE_LOCATION
        val permStatus: Int = ContextCompat.checkSelfPermission(requireContext(), locationPermission)
        if (permStatus != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(locationPermission)) {
                showWriteExternalPermissionRationale()
            } else {
                requestLocationPermission.launch(locationPermission)
            }
        } else {
            //get current location
            getCurrentLocation()
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        //fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, cancelableTask)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null) {
                    Log.d(TAG, "Location: ${location.latitude} ${location.longitude}")
                    currentLocation = com.ybouidjeri.weatherapp.models.Location(location.latitude, location.longitude)
                    weatherInfoList[0].city.location = currentLocation
                    listViewModel.getWeather(location.latitude, location.longitude)
                }
            }
    }


    private fun showWriteExternalPermissionRationale() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialog.setTitle(getString(R.string.dialog_location_permission))
        dialog.setMessage(getString(R.string.msg_permission_required))
        dialog.setPositiveButton(R.string.yes) { _, _ ->
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        dialog.setNegativeButton(R.string.no) { _, _ -> }
        dialog.show()
    }

}