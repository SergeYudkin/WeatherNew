package com.example.weathernew.repository

import com.example.weathernew.BuildConfig
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.YANDEX_API_URL
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepositoryRemoteImpl: RepositoryDetails {


   private val retrofit = Retrofit.Builder().baseUrl(YANDEX_API_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create())
        ).build().create(WeatherApi::class.java)



    override fun getWeatherFromServer(lat:Double,lon:Double, callback: Callback<WeatherDTO>) {

        retrofit.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(callback)
    }


}



