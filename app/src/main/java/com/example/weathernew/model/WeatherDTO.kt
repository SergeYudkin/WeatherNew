package com.example.weathernew.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherDTO(

    val fact: Fact,
): Parcelable

@Parcelize
data class Fact (

    val temp: Long,

    @SerializedName("feels_like")
    val feelsLike: Long,

    val icon: String,
    val condition: String,

    ): Parcelable