package com.example.baselib.liveData

import androidx.lifecycle.LiveData
import com.example.baselib.entity.BaseResult
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// 采用的LiveData来承载解析后的数据，所以需要自定义adapter来与retrofit适配。
// 这个类的主要作用是对网络请求后解析出来的数据进行判断，是否符合我们预期的数据。
class LiveDataCallAdapterFactory :CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) !=LiveData::class.java)
            return null
        //获取第一个泛型类型的数据
        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        //获取泛型的class
        val rawType = getRawType(observableType)
        //判断是类型一致
        if (rawType != BaseResult::class.java) {
            throw IllegalArgumentException("type must be ApiResponse")
        }
        if (observableType !is ParameterizedType) {
            throw IllegalArgumentException("resource must be parameterized")
        }
        return LiveDataCallAdapter<Any>(observableType)
    }
}