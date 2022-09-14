package com.example.coolweather.repository

import androidx.lifecycle.LiveData
import com.example.baselib.entity.BaseResult
import com.example.coolweather.data.CityWeatherDao
import com.example.coolweather.entity.AirEntity
import com.example.coolweather.entity.DailyEntity
import com.example.coolweather.entity.HourlyEntity
import com.example.coolweather.entity.NowEntity
import com.example.coolweather.data.ApiService
import javax.inject.Inject

/**
 * 天气相关的仓库类，负责从网络获取天气相关的数据
 * 针对不同的类型数据去不同Repository获取
 */
class WeatherRepository @Inject constructor(private val weatherDao: CityWeatherDao,
                                            private val apiService: ApiService
): Repository {

    fun getNowWeatherInfo(map: Map<String, String>):LiveData<BaseResult<NowEntity>>{
        return apiService.getNoWData(map)
    }

    fun getFutureWeatherList(map: Map<String, String>):LiveData<BaseResult<List<DailyEntity>>>{
        return apiService.getFutureWeatherList(map)
    }

    fun getAirNowInfo(map: Map<String, String>):LiveData<BaseResult<AirEntity>>{
        return apiService.getAriNow(map)
    }

    fun getHourlyList(map: Map<String, String>):LiveData<BaseResult<List<HourlyEntity>>>{
        return apiService.getHourlyList(map)
    }

}