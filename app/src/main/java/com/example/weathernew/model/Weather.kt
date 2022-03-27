package com.example.weathernew.model

data class Weather(val city:City = getDefaultCity(),val temperature:Int=8, val feelsLike:Int=4)

data class City(val name:String, val lat:Double,val lon:Double)



fun getDefaultCity() = City("Волгоград",48.7194,44.5018)
