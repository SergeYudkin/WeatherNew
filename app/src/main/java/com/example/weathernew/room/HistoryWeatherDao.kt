package com.example.weathernew.room

import android.database.Cursor
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


    /** Lesson 9 **/
    @Query("DELETE FROM history_weather_entity WHERE id=:id")
    fun delete (id : Long)                                             // удалить по id

    @Query("SELECT * FROM history_weather_entity WHERE id=:id")
    fun getHistoryCursor (id : Long): Cursor
    /** Lesson 9 **/

}