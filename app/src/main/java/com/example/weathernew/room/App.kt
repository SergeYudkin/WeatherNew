package com.example.weathernew.room

import android.app.Application
import android.icu.util.IllformedLocaleException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.weathernew.utils.DB_NAME

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object{
        private var appInstance:App? = null
        private var db:HistoryDatabase? = null

        @RequiresApi(Build.VERSION_CODES.P)
        fun getHistoryWeatherDao():HistoryWeatherDao{
            if (db==null){
                if (appInstance==null){
                    throw IllformedLocaleException("Всё очень плохо")
                }else{
                    db = Room.databaseBuilder(appInstance!!.applicationContext,HistoryDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()   // по дз надо убрать, убираешь падает с ошибкой блокировки главного потока. Хотя в HistoryViewModel тело функции getAllHistory() обернули в поток
                        .build()
                }
            }
            return db!!.historyWeatherDao()
        }

    }

}