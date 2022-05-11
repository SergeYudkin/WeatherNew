package com.example.weathernew.repository

import com.example.weathernew.model.WeatherDTO

interface RepositoryDetails {

    fun getWeatherFromServer(lat:Double,lon:Double,callback: retrofit2.Callback<WeatherDTO>)


}