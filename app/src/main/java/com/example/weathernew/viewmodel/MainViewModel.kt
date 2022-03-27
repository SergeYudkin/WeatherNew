package com.example.weathernew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.Thread.sleep

class MainViewModel(val liveData:MutableLiveData<Any> = MutableLiveData()) : ViewModel() {

//-------------------------------------------------------------------------------------
/* LiveData — — объект, который хранит данные. На LiveData можно подписаться и получать
уведомления, если данные, которые хранит LiveData, изменились, например, пришли новые
данные из интернета. LiveData знает о жизненном цикле Activity или фрагмента благодаря
LifecycleOwner. Это позволяет не обновлять данные в Activity, если Activity уничтожена, и
защищает от утечек памяти.*/
    fun getLivaData(): LiveData<Any>{
        return liveData
    }
//--------------------------------------------------------------------------------------

    fun getWeatherFromServer(){
        Thread{
            sleep(2000)
                //liveData.value = Any()       // асинхронный запрос
                liveData.postValue(Any())    // синхронный запрос
        }.start()
    }
}