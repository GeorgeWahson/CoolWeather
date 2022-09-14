package com.example.coolweather.utils

import com.example.coolweather.R
import com.example.coolweather.CoolWeatherApplication
import java.lang.StringBuilder

object IconUtils {
    fun getSmallIcon(code: Int):Int{
        // https://dev.qweather.com/docs/resource/icons/
        // 根据返回值设置相应天气图标
        val context = CoolWeatherApplication.getContext()
        var builder=StringBuilder("s_")// 官网下载的图标带“s_”
        builder.append(code)
        val id=context.resources.getIdentifier(builder.toString(),"drawable",context.packageName)

        return if (id==-1) R.drawable.s_unknown else id
    }

    fun getWindIcon(content:String):Int{
        return when(content){
            "北风"-> R.drawable.icon_wind_north
            "东北风"-> R.drawable.icon_wind_east_north
            "东风"-> R.drawable.icon_wind_east
            "东南分"-> R.drawable.icon_wind_east_south
            "南风"-> R.drawable.icon_wind_south
            "西南风"-> R.drawable.icon_wind_west_south
            "西风"-> R.drawable.icon_wind_west
            "西北风" -> R.drawable.icon_wind_west_north
            else
            -> R.drawable.icon_wind_north
        }
    }

}