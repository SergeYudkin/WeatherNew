package com.example.weathernew.room

import androidx.room.*
@Dao
interface HistoryWeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryWeatherEntity)        // вставляем данные

    @Delete
    fun delete (entity: HistoryWeatherEntity)      // удалить

    @Update
    fun update (entity: HistoryWeatherEntity)      // обновить


    @Query("select * FROM history_weather_entity")
    fun getAllHistoryWeather():List<HistoryWeatherEntity>     // получаем данные

      // fun getOllHistoryWeather()  //todo получить по какому то полю

}