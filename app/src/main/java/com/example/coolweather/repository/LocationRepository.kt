package com.example.coolweather.repository

import androidx.lifecycle.LiveData
import com.example.baselib.entity.BaseResult
import com.example.coolweather.data.CityDao
import com.example.coolweather.entity.Location
import com.example.coolweather.data.ApiService
import javax.inject.Inject
import javax.inject.Named


class LocationRepository @Inject constructor(private val cityDao: CityDao, @Named("city") private var  apiService: ApiService):
    Repository {

    fun getCityInfo(map: Map<String, String>):LiveData<BaseResult<List<Location>>>{
        return apiService.getCityInfo(map)
    }

    fun searchCity(map: Map<String, String>):LiveData<BaseResult<List<Location>>>{
        return apiService.searchCity(map)
    }

}