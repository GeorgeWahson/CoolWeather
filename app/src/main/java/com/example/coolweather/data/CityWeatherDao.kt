package com.example.coolweather.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityWeatherDao {

    @Query("SELECT * FROM CityWeather WHERE id=:id")
    fun getWeatherById(id:Int): CityWeather

    @Query("SELECT * FROM CityWeather")
    fun getAllWeather(): LiveData<List<CityWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(data: CityWeather)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateData(data: CityWeather)
}