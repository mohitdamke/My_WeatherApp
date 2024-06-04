package com.example.weatherapp

import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.api.Constant
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.RetrofitInstance
import com.example.weatherapp.api.WeatherModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Retrofit

class WeatherViewModel : ViewModel() {

    private val WeatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.loading

            try {
                val response = WeatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.failed("Failed To Load")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.failed("Failed To Load")
            }
        }
    }
}