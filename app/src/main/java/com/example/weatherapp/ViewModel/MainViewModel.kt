package com.example.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.Model.Entities.Response
import com.example.weatherapp.Model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val repository: WeatherRepository = WeatherRepository()

    private var _currentWeather: MutableLiveData<Response> = MutableLiveData()
    val currentWeather:LiveData<Response>
        get()=_currentWeather

    fun getWeather(cityName: String)
    {
        viewModelScope.launch {
            _currentWeather.value = repository.getWeather(cityName)
        }
    }
}