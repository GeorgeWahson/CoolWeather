package com.example.coolweather.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.example.baselib.entity.BaseResult
import com.example.coolweather.view.BaseViewModel
import com.example.coolweather.entity.AirEntity
import com.example.coolweather.entity.DailyEntity
import com.example.coolweather.entity.HourlyEntity
import com.example.coolweather.entity.NowEntity
import com.example.coolweather.repository.WeatherRepository

class WeatherViewModel @ViewModelInject constructor(private val weatherRepository: WeatherRepository) :
    BaseViewModel(){


    fun getWeatherNowInfo(id:String):LiveData<BaseResult<NowEntity>>{
       return weatherRepository.getNowWeatherInfo(getParams(id))

    }

    fun getFutureWeatherList(id:String):LiveData<BaseResult<List<DailyEntity>>>{
        val map=getParams(id)
        return weatherRepository.getFutureWeatherList(map)
    }

    fun getAirNowInfo(id:String):LiveData<BaseResult<AirEntity>>{
        return weatherRepository.getAirNowInfo(getParams(id))
    }


    fun getHourlyList(id: String):LiveData<BaseResult<List<HourlyEntity>>>{
        return weatherRepository.getHourlyList(getParams(id))
    }




}