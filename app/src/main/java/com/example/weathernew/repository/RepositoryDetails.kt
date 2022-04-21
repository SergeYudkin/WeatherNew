package com.example.weathernew.repository

import com.example.weathernew.model.Weather
import okhttp3.Callback

interface RepositoryDetails {

    fun getWeatherFromServer(url:String,callback: Callback)


}