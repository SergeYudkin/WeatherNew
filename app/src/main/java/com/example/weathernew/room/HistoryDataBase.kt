package com.example.weathernew.room

import androidx.room.Database
import androidx.room.RoomDatabase


// этот класс должен быть абстрактным
@Database(entities = [HistoryWeatherEntity::class], version = 1, exportSchema = false)     // таблицы передаются массивами
abstract class HistoryDatabase:RoomDatabase() {

    abstract fun historyWeatherDao():HistoryWeatherDao

}