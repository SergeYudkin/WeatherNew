package com.example.weathernew.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_weather_entity")
data class HistoryWeatherEntity(                                                                              // шаблон таблички
    @PrimaryKey(autoGenerate = true)
    var id:Long=0,
    var name: String="",
    var temperature:Int=0,
    var feelsLike:Int=0,
    var icon:String="")

