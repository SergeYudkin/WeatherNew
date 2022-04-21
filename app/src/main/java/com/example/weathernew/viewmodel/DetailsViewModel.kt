package com.example.weathernew.viewmodel

import android.icu.util.UniversalTimeScale.toLong
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.R
import com.example.weathernew.model.Fact
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.model.getDefaultCity
import com.example.weathernew.repository.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
       repositoryImpl.getWeatherFromServer(lat, lon,callback)
    }

   /* fun converterDTOtoModel(weatherDTO: WeatherDTO):List<Weather>{
        return listOf(Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt(),weatherDTO.fact.icon))
    }*/


    private val callback =  object : Callback <WeatherDTO>{
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {   // сюда приходит ответ от RepositoryImpl(жирного репозитория)
            liveData.postValue(AppState.Error(R.string.errorCode,418))
        }

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppState.SuccessCity(listOf(
                        Weather(getDefaultCity(),
                        it.fact.temp.toInt(), it.fact.feelsLike.toInt(),it.fact.icon)
                    )))

                }

            }else{
                liveData.postValue(AppState.Error(R.string.errorCode,response.code()))
            }

        }
    }
}