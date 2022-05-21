package com.example.weathernew.room

import android.app.Application
import android.icu.util.IllformedLocaleException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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

//-----------------------------------------------------------------------------------------------------------------------------------------
                        .addMigrations(object : Migration(1,2){                     // миграция с версии 1 на версию 2, в нашем случае добавилась колонка icon2
                            override fun migrate(database: SupportSQLiteDatabase) {
                                database.execSQL("ALTER TABLE history_weather_entity ADD COLUMN icon2 TEXT NOT NULL DEFAULT '' ")
                            }
                        })
//--------------------------------------------------------------------------------------------------------------------------------------

                        .build()
                }
            }
            return db!!.historyWeatherDao()
        }

    }

}