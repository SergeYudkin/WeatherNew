package com.example.weathernew.viewmodel

import com.example.weathernew.model.Weather


sealed class AppState {
    data class Loading(val progress: Int): AppState()
    data class SuccessDetails(val  weatherData:List<Weather>):AppState()
    data class SuccessCity(val weatherData: List<Weather>):AppState()
     data class Error(val error: Int): AppState()


}