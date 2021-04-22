package com.example.weatherapp.Model.Entities

data class Response(
    val name: String,
    val timezone:  Int,
    val dt: Long,
    val weather: ArrayList<Weather>,
    val main: Main,
    val sys: Sys
)
