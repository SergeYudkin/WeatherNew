package com.example.weathernew.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weathernew.BuildConfig
import com.example.weathernew.model.*
import com.example.weathernew.room.App
import com.example.weathernew.room.HistoryWeatherEntity
import com.example.weathernew.utils.API_KEY
import com.example.weathernew.utils.YANDEX_API_URL
import com.example.weathernew.utils.YANDEX_API_URL_END_POINT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

import java.io.IOException

class RepositoryLocalImpl: RepositoryCitiesList,RepositoryHistoryWeather {



    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getAllHistoryWeather(): List<Weather> {
        return converterHistoryWeatherEntityToWeather (App.getHistoryWeatherDao().getAllHistoryWeather())
    }

    private fun converterHistoryWeatherEntityToWeather(entityList: List<HistoryWeatherEntity>): List<Weather>{
        return entityList.map{
            Weather(City(it.city,0.0,0.0),it.temperature,
                it.feelsLike,it.icon) }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun saveWeather(weather: Weather) {
        converterWeatherToHistoryWeatherEntity(weather)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun converterWeatherToHistoryWeatherEntity(weather: Weather) {
        App.getHistoryWeatherDao().insert(
            HistoryWeatherEntity(
                0, weather.city.name,
                weather.temperature, weather.feelsLike, weather.icon
            )
        )
    }


}



