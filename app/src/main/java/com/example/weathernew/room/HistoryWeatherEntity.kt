package com.example.weathernew.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weathernew.model.City

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(                                                                              // шаблон таблички
    @PrimaryKey(autoGenerate = true) val id:Long, val city: String,val temperature:Int, val feelsLike:Int, val icon:String)

