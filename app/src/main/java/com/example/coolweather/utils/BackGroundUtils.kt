package com.example.coolweather.utils

import com.example.coolweather.R

/**
 * 获取背景的工具类
 */
object BackGroundUtils{

    fun getBackGroundByCode(code:Int):Int{

        // 根据now.icon的值确定天气背景https://dev.qweather.com/docs/resource/icons/
        if (code==100){ // 白天-晴天
            return R.drawable.bg_day_sun
        }
        if (code==150){ // 夜晚-晴天
            return R.drawable.bg_night_sun
        }
        if (code in 101..103){ // 白天-多云
            return R.drawable.bg_day_cloudy
        }
        if (code in 151..153){ // 夜晚-多云
            return R.drawable.bg_night_cloudy
        }
        if (code==104){ // 白天-阴天
            return R.drawable.bg_day_yin
        }
        if (code==154){ // 夜晚-阴天
            return R.drawable.bg_night_yin
        }
        if (code in 300..399){ //下雨
            return R.drawable.bg_rain
        }

        if (code in 400..499){  // 下雪
            return R.drawable.bg_snow
        }
        if (code in 500..515){  // 雾霾
            return R.drawable.bg_fog
        }

        return R.drawable.bg_default // 默认背景
    }

}