package com.example.coolweather.entity

import com.google.gson.annotations.SerializedName

data class HeWeatherCity6( @SerializedName("basic") val basics:List<CityBasic> )