package com.example.weathernew.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import com.example.weathernew.BuildConfig

import com.example.weathernew.model.WeatherDTO
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onWeatherLoaded:OnWeatherLoaded) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(lat:Double, lon:Double) {                          // запрос погоды с сервера
        Thread {
            try {
                val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
                val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"          // открываем соединение гет запрос
                    readTimeout = 2000
                    addRequestProperty(
                        API_KEY,
                        BuildConfig.WEATHER_API_KEY
                    )      // передаём ключ апи
                }

                val bufferedReader =
                    BufferedReader(InputStreamReader(httpsURLConnection.inputStream))    //  Connection счатывает в буфер строку
                val weatherDTO: WeatherDTO? = Gson().fromJson(
                    convertBufferToResult(bufferedReader),                              // конвертер пихает в буфер большой стринг, далее Gson переводит большую строку в WeatherDTO
                    WeatherDTO::class.java
                )
                Handler(Looper.getMainLooper()).post {                                      // реализацию интерфейса обернули в хендлер так как она должна происходить в главном потоке
                    onWeatherLoaded.onLoaded(weatherDTO)                                   // реализацию интерфейса передаём в weatherDTO
                }
            }catch (e: Exception){
                onWeatherLoaded.onFailed("Error", Snackbar.LENGTH_LONG)
            }

        }.start()

    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @RequiresApi(Build.VERSION_CODES.N)
    private  fun  convertBufferToResult(bufferedReader:BufferedReader):String{     // конвертер, сбор данных в одну большую строку
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    interface OnWeatherLoaded{
    fun onLoaded(weatherDTO: WeatherDTO?)

    fun onFailed(text:String, length:Int){

    }
    }


}