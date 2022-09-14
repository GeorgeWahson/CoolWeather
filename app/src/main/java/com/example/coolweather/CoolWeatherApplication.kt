package com.example.coolweather

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.baselib.utils.ToastUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoolWeatherApplication :Application(){

    companion object{
        var instance:CoolWeatherApplication?=null

        fun getContext():Context{
            return instance!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        instance=this
        ToastUtil.init(this)
        MultiDex.install(this)

    }
}