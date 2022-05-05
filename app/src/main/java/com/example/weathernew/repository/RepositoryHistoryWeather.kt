package com.example.weathernew.repository

import com.example.weathernew.model.Weather

interface RepositoryHistoryWeather {

    fun getAllHistoryWeather():List<Weather>
    fun saveWeather(weather: Weather)

}