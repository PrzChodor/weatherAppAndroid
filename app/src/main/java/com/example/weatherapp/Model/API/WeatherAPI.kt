package com.example.weatherapp.Model.API

import com.example.weatherapp.Model.Entities.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("weather?appid=a599189cf2973ef94f4f439e2f79b198&units=metric&lang=pl")
    suspend fun getWeather(@Query("q") cityName : String): Response
}