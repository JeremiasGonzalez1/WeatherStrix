package com.jg.weatherstrix.data.repository

import com.jg.weatherstrix.data.interfaces.WeatherDataSourceLocal
import com.jg.weatherstrix.domain.interfaces.DeleteFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.interfaces.GetFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.interfaces.SetFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.models.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(private val dataSourceLocal: WeatherDataSourceLocal):
    SetFavoriteRepositoryStorage, DeleteFavoriteRepositoryStorage, GetFavoriteRepositoryStorage
{
    override suspend fun setFavorite(weather: Weather) {
        dataSourceLocal.saveWeather(weather)
    }

    override suspend fun deleteFavorite(weather: Weather) {
        dataSourceLocal.deleteWeather(weather)
    }

    override suspend fun getFavorites(): Flow<List<Weather>> {
        return dataSourceLocal.getFavorites()
    }

}