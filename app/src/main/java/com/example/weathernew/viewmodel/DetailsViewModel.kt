package com.example.weathernew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.model.getDefaultCity
import com.example.weathernew.repository.RepositoryImpl
import com.example.weathernew.utils.YANDEX_API_URL
import com.example.weathernew.utils.YANDEX_API_URL_END_POINT
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.Thread.sleep

class DetailsViewModel(
     val liveData:MutableLiveData<AppState> = MutableLiveData(),
    )
    : ViewModel() {

    private val repositoryImpl: RepositoryImpl by lazy {
        RepositoryImpl()
    }
    fun getLivaData() = liveData



     fun getWeatherFromRemoteServer(lat: Double, lon: Double){
        liveData.postValue(AppState.Loading(0))
       repositoryImpl.getWeatherFromServer(
           YANDEX_API_URL + YANDEX_API_URL_END_POINT
               + "?lat=${lat}&lon=${lon}",callback)
    }

    fun converterDTOtoModel(weatherDTO: WeatherDTO):List<Weather>{
        return listOf(Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt()))
    }


    private val callback =  object : Callback {
        override fun onFailure(call: Call, e: IOException) {   // сюда приходит ответ от RepositoryImpl(жирного репозитория)
            TODO("Not yet implemented")
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful){
                response.body()?.let {
                    val json  = it.string()
                    liveData.postValue(AppState.Success(converterDTOtoModel(Gson().fromJson(json, WeatherDTO::class.java))))

                }

            }else{
                
            }
    }   }

}