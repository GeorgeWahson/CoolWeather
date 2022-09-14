package com.example.coolweather.module

import com.example.coolweather.data.CityDao
import com.example.coolweather.data.ApiService
import com.example.coolweather.repository.LocationRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Singleton
    @ActivityRetainedScoped
    fun  providerLocationRepository(cityDao: CityDao, apiService: ApiService): LocationRepository {
        return LocationRepository(cityDao, apiService)
    }

}