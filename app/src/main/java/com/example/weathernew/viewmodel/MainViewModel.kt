package com.example.weathernew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.model.RepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData:MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()) : ViewModel() {

//-------------------------------------------------------------------------------------
/* LiveData — — объект, который хранит данные. На LiveData можно подписаться и получать
уведомления, если данные, которые хранит LiveData, изменились, например, пришли новые
данные из интернета. LiveData знает о жизненном цикле Activity или фрагмента благодаря
LifecycleOwner. Это позволяет не обновлять данные в Activity, если Activity уничтожена, и
защищает от утечек памяти.*/
    fun getLivaData(): LiveData<AppState>{
        return liveData
    }
//--------------------------------------------------------------------------------------

    fun getWeatherFromLocalStorageRus() = getWeatherFromLocalServer(true)

    fun getWeatherFromLocalStorageWorld() = getWeatherFromLocalServer(false)

    fun getWeatherFromRemoteSource() = getWeatherFromLocalServer(true) // заглушка на пятый урок



    private fun getWeatherFromLocalServer(isRussian: Boolean){
        liveData.postValue(AppState.Loading(0))
        Thread{
            sleep(1000)

            if (true){
                liveData.postValue(
                    AppState.Success(
                        if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                        else {
                            repositoryImpl.getWeatherFromLocalStorageWorld()
                        }
                    )
                )


            }else{
              //  liveData.postValue(AppState.Error(IllegalStateException("Ошибка")))
            }

        }.start()
    }


}