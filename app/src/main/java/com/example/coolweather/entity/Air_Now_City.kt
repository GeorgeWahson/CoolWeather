package com.example.coolweather.entity

data class Air_Now_City(
    // https://dev.qweather.com/docs/api/air/air-now/
    val aqi: String,
    val co: String,
    val main: String,
    val no2: String,
    val o3: String,
    val pm10: String,
    val pm25: String,
    val pub_time: String,
    val qlty: String,
    val so2: String
)