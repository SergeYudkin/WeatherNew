package com.example.weathernew.repository

import com.example.weathernew.BuildConfig
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.model.getRussianCities
import com.example.weathernew.model.getWorldCities
import com.example.weathernew.utils.API_KEY
import com.example.weathernew.utils.YANDEX_API_URL
import com.example.weathernew.utils.YANDEX_API_URL_END_POINT
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepositoryImpl: RepositoryCitiesList, RepositoryDetails {



    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()


    override fun getWeatherFromServer(url: String, callback: Callback) {
        val builder = Request.Builder().apply {
            header(API_KEY, BuildConfig.WEATHER_API_KEY)
            url(url)


        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }


}



