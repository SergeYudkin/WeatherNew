package com.example.weathernew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(val liveData:MutableLiveData<AppState> = MutableLiveData()) : ViewModel() {

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

    fun getWeatherFromServer(){
        liveData.postValue(AppState.Loading(0))
        Thread{

            sleep(3000)
            liveData.postValue(AppState.Success("Холодно"))
        }.start()
    }
}