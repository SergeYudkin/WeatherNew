package com.example.weathernew.repository

import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import okhttp3.Callback

interface RepositoryDetails {

    fun getWeatherFromServer(lat:Double,lon:Double,callback: retrofit2.Callback<WeatherDTO>)


}