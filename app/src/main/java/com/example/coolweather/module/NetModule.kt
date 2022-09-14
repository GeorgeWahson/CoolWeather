package com.example.coolweather.module

import com.example.baselib.liveData.LiveDataCallAdapterFactory
import com.example.baselib.utils.L
import com.example.coolweather.data.Api
import com.example.coolweather.data.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * 网络相关的module 对外提供retrofit
 * 目前项目中的API还没有迁移完，所以需要提供不同版本的retrofit
 */
@Module
@InstallIn(ApplicationComponent::class)
object NetModule {
    // 对retrofit进行封装
    @Provides
    @Singleton
    fun providerOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(initLogInterceptor())
        return builder.build()
    }
    // 对Retrofit进行初始化，添加解析器和LiveData的适配器
    @Provides
    @Singleton
    fun providerRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.CITY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Named("retrofit2")
    @Provides
    @Singleton
    fun providerRetrofit2(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    /**
     *提供城市的retrofit
     */
    @Named("cityRetrofit")
    @Provides
    @Singleton
    fun providerCityRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.CITY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }
    // 给外部提供API接口的方法
    @Provides
    @Singleton
    fun providerApiService2(@Named("retrofit2") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Named("city")
    @Provides
    @Singleton
    fun providerApiService2ByCity(@Named("cityRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    /**
     * 初始化日志输出
     */
    private fun initLogInterceptor(): Interceptor {

        val loggingInterceptor = HttpLoggingInterceptor { message -> L.i( message) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return loggingInterceptor
    }
}