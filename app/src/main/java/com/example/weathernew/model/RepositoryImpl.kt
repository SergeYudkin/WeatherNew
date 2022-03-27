package com.example.weathernew.model

class RepositoryImpl: Repository{
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }

}