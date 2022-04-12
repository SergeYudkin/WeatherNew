package com.example.weathernew.utils

import android.os.Handler
import android.os.Looper
import com.example.weathernew.model.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(private val onWeatherLoaded:OnWeatherLoaded) {

    fun loadWeather(lat:Double, lon:Double) {                          // запрос погоды с сервера
        Thread {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
            val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                requestMethod = "GET"          // открываем соединение гет запрос
                readTimeout = 2000
                addRequestProperty(
                    "X-Yandex-API-Key",
                    "8a6d62b9-8c7c-4b42-a829-789b9767b393"              // передаём ключ апи
                )
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

        }.start()

    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private  fun  convertBufferToResult(bufferedReader:BufferedReader):String{     // конвертер, сбор данных в одну большую строку
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    interface OnWeatherLoaded{
        fun onLoaded(weatherDTO: WeatherDTO?)
        fun onFailed() // TODO  д/з
    }

}