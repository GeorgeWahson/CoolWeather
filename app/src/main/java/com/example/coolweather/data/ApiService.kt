package com.example.coolweather.data

import androidx.lifecycle.LiveData
import com.example.baselib.entity.BaseResult
import com.example.coolweather.entity.*
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * 和风天气API接口
 */
interface ApiService {

    /**
     * 获取实时天气
     */
    @GET("weather/now")
    fun getNoWData(@QueryMap map: Map<String, String>): LiveData<BaseResult<NowEntity>>


    /**
     * 搜索城市
     */
    @GET("city/lookup")
    fun searchCity(@QueryMap map: Map<String, String>):LiveData<BaseResult<List<Location>>>


    /**
     * 获取城市信息
     */
    @GET("city/lookup")
    fun getCityInfo(@QueryMap map: Map<String, String>): LiveData<BaseResult<List<Location>>>

    /**
     * 获取一周天气信息
     */
    @GET("weather/7d")
    fun getFutureWeatherList(@QueryMap map: Map<String, String>):LiveData<BaseResult<List<DailyEntity>>>


    /**
     * 获取当前空气质量实体类
     */
    @GET("air/now")
    fun getAriNow(@QueryMap map: Map<String, String>):LiveData<BaseResult<AirEntity>>

    /**
     * 获取未来24小时天气信息
     */
    @GET("weather/24h?")
    fun getHourlyList(@QueryMap map: Map<String, String>):LiveData<BaseResult<List<HourlyEntity>>>

}