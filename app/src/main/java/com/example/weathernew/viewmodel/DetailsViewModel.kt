package com.example.weathernew.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.R
import com.example.weathernew.model.Weather
import com.example.weathernew.model.WeatherDTO
import com.example.weathernew.model.getDefaultCity
import com.example.weathernew.repository.RepositoryLocalImpl
import com.example.weathernew.repository.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsViewModel(
     val liveData:MutableLiveData<AppState> = MutableLiveData(),
     private val repositoryLocalImpl: RepositoryLocalImpl = RepositoryLocalImpl()
    )
    : ViewModel() {

    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }


    fun getLivaData() = liveData

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveWeather(weather: Weather){
        Thread{
            repositoryLocalImpl.saveWeather(weather)
        }.start()

    }



     fun getWeatherFromRemoteServer(lat: Double, lon: Double){
        liveData.postValue(AppState.Loading(0))
       repositoryRemoteImpl.getWeatherFromServer(lat, lon,callback)
    }



    private val callback =  object : Callback <WeatherDTO>{
        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            liveData.postValue(AppState.Error(R.string.errorCode))
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
                liveData.postValue(AppState.Error(R.string.errorCode))
            }

        }
    }
}