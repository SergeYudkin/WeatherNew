package com.example.weathernew.view.details

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weathernew.BuildConfig
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.utils.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class DetailsService(name:String = ""): IntentService(name) {



    override fun onHandleIntent(intent: Intent?) {
        intent?.let {

            val lat = intent.getDoubleExtra(BUNDLE_KEY_LAT,0.0)
            val lon = intent.getDoubleExtra(BUNDLE_KEY_LON,0.0)      // считываем координаты переданные из детеилс фрагмента
            loadWeather(lat, lon)
        }

    }
//-----------------------------------------------------------------------------------------------------------------------------------------------
    private fun loadWeather(lat:Double, lon:Double) {                          // запрос погоды с сервера

            try {
                val url = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
                Thread {

                    val httpsURLConnection = (url.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"          // открываем соединение гет запрос
                        readTimeout = 2000
                        addRequestProperty(
                            API_KEY,
                            BuildConfig.WEATHER_API_KEY
                        )             // передаём ключ апи
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(httpsURLConnection.inputStream))    //  Connection счатывает в буфер строку
                    val weatherDTO: WeatherDTO? = Gson().fromJson(
                        convertBufferToResult(bufferedReader), WeatherDTO::class.java)     // конвертер пихает в буфер большой стринг, далее Gson переводит большую строку в WeatherDTO WeatherDTO::class.java)


                    /* sendBroadcast( Intent(BROADCAST_KEY).apply {      // кричим на всё устройство
                        putExtra(BUNDLE_KEY_WEATHER,weatherDTO)        // послали сообщение
                    })*/

                    val intent = Intent(DETAILS_INTENT_FILTER).apply {
                        putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
                    }

                    /*LocalBroadcastManager.getInstance(applicationContext)
                        .sendBroadcast(Intent(BROADCAST_KEY).apply {      // кричим локально в рамках приложения
                            putExtra(BUNDLE_KEY_WEATHER, weatherDTO)
                        })*/
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                    httpsURLConnection.disconnect()

                }.start()

            }catch (e:Exception){
               e.printStackTrace()
            }
    }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private  fun  convertBufferToResult(bufferedReader: BufferedReader):String{     // конвертер, сбор данных в одну большую строку
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

}