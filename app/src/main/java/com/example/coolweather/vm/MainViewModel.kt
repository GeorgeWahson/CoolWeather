package com.example.coolweather.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    private var mRefreshLiveData=MutableLiveData<Int>()

     fun setRefresh(entity:Int){
        mRefreshLiveData.value=entity
    }

}