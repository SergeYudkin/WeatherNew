package com.example.weathernew.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weathernew.model.City
import com.example.weathernew.model.Weather
import com.example.weathernew.model.getRussianCities
import com.example.weathernew.model.getWorldCities
import com.example.weathernew.room.App
import com.example.weathernew.room.HistoryWeatherEntity

class RepositoryLocalImpl: RepositoryCitiesList,RepositoryHistoryWeather {



    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getAllHistoryWeather(): List<Weather> {
        return converterHistoryWeatherEntityToWeather (App.getHistoryWeatherDao().getAllHistoryWeather())
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun saveWeather(weather: Weather) {
        App.getHistoryWeatherDao().insert(converterWeatherToHistoryWeatherEntity(weather))

    }


    private fun converterHistoryWeatherEntityToWeather(entityList: List<HistoryWeatherEntity>): List<Weather>{
        return entityList.map{
            Weather(City(it.name,0.0,0.0),it.temperature,
                it.feelsLike,it.icon) }
    }




    @RequiresApi(Build.VERSION_CODES.P)
    private fun converterWeatherToHistoryWeatherEntity(weather: Weather) =
            HistoryWeatherEntity(
                0, weather.city.name,
                weather.temperature, weather.feelsLike, weather.icon
            )

}



