package com.example.weatherapp.Model

import android.util.Log
import com.example.weatherapp.Model.API.WeatherAPI
import com.example.weatherapp.Model.API.WeatherClient
import com.example.weatherapp.Model.Entities.Main
import com.example.weatherapp.Model.Entities.Response
import com.example.weatherapp.Model.Entities.Sys
import com.example.weatherapp.Model.Entities.Weather
import java.lang.Exception

class WeatherRepository {
    var client: WeatherAPI = WeatherClient.retrofit

    suspend fun getWeather(cityName: String): Response{
        return try {
            client.getWeather(cityName)
        } catch (e: Exception) {
            e.message?.let { Log.d("IKS", it) }
            Response("Nie znaleziono",0,0, arrayListOf(Weather("","01d")), Main(0.0,0),Sys(0,0))
        }
    }
}