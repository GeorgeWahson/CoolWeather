package com.example.coolweather.view

import androidx.lifecycle.ViewModel
import com.example.coolweather.data.ApiKey

open class BaseViewModel :ViewModel(){

    open fun getParams(location:String):Map<String, String>{
        return mutableMapOf("location" to location,"key" to ApiKey.API_KEY)
    }
}