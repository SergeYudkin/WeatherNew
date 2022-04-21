package com.example.weathernew.repository

import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.API_KEY
import com.example.weathernew.utils.YANDEX_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {

    @GET(YANDEX_API_URL_END_POINT)

    fun getWeather(@Header(API_KEY) apikey:String
                   , @Query("lat") lat:Double
                   ,@Query("lon") lon:Double)
                    : Call<WeatherDTO>

}