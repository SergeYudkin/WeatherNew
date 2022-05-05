package com.example.weathernew.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathernew.repository.RepositoryLocalImpl
import com.example.weathernew.repository.RepositoryRemoteImpl
import java.lang.Thread.sleep

class HistoryViewModel(
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

    @RequiresApi(Build.VERSION_CODES.P)
    fun getAllHistory(){
       // liveData.postValue(AppState.Loading(0))

        Thread{
            val listWeather =  repositoryLocalImpl.getAllHistoryWeather()
            liveData.postValue(AppState.SuccessDetails(listWeather))
        }.start()

    }

}