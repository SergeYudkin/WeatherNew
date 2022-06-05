package com.example.weathernew.repository

import com.example.weathernew.model.Weather

interface RepositoryCitiesList {

    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}


