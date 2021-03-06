package com.example.weathernew.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.repository.RepositoryLocalImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveData:MutableLiveData<AppState> = MutableLiveData(),
    )
    : ViewModel() {

    private val repositoryLocalImpl: RepositoryLocalImpl by lazy {
        RepositoryLocalImpl()
    }

//-------------------------------------------------------------------------------------
/* LiveData — — объект, который хранит данные. На LiveData можно подписаться и получать
уведомления, если данные, которые хранит LiveData, изменились, например, пришли новые
данные из интернета. LiveData знает о жизненном цикле Activity или фрагмента благодаря
LifecycleOwner. Это позволяет не обновлять данные в Activity, если Activity уничтожена, и
защищает от утечек памяти.*/
    fun getLivaData() = liveData

//--------------------------------------------------------------------------------------

    fun getWeatherFromLocalStorageRus() = getWeatherFromLocalServer(true)

    fun getWeatherFromLocalStorageWorld() = getWeatherFromLocalServer(false)



    private fun getWeatherFromLocalServer(isRussian: Boolean){
        liveData.postValue(AppState.Loading(0))
        Thread{
            sleep(1000)

                liveData.postValue(
                    AppState.SuccessDetails(
                        with(repositoryLocalImpl){
                            if (isRussian) {
                                getWeatherFromLocalStorageRus()
                            }else {
                                getWeatherFromLocalStorageWorld()
                            }
                        }
                    )
                )

        }.start()
    }

}