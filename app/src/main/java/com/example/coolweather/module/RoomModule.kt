package com.example.coolweather.module

import android.app.Application
import androidx.room.Room
import com.example.coolweather.data.AppDataBase
import com.example.coolweather.data.CityDao
import com.example.coolweather.data.CityWeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {


    /**
     * @Provides 常用于被 @Module 注解标记类的内部的方法，并提供依赖项对象。
     * @Singleton 提供单例
     */
    @Provides
    @Singleton
    fun providerAppDataBase(application: Application): AppDataBase {
        return Room.databaseBuilder(application, AppDataBase::class.java,"City.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
    }

    @Provides
    @Singleton
    fun providerCityDao(appDataBase: AppDataBase): CityDao {
        return appDataBase.getCityDao()
    }

    @Provides
    @Singleton
    fun providerWeatherDao(appDataBase: AppDataBase): CityWeatherDao {
        return appDataBase.getCityWeatherDao()
    }
}