package com.jg.weatherstrix.data.datasource

import com.jg.weatherstrix.data.database.WeatherFavoriteDao
import com.jg.weatherstrix.data.interfaces.WeatherDataSourceLocal
import com.jg.weatherstrix.data.utils.toEntity
import com.jg.weatherstrix.data.utils.toModel
import com.jg.weatherstrix.domain.models.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherDataSourceLocalImpl @Inject constructor (private val db: WeatherFavoriteDao) :WeatherDataSourceLocal{
    override suspend fun saveWeather(weatherModel: Weather) {
        withContext(Dispatchers.IO){
            db.save(weatherModel.toEntity())
        }
    }

    override suspend fun deleteWeather(weatherModel: Weather) {
        withContext(Dispatchers.IO){
            db.delete(weatherModel.toEntity())
        }
    }

    override suspend fun getFavorites(): Flow<List<Weather>> =
        withContext(Dispatchers.IO) {
            db.getWeathers().map {
                it.map { weatherEntity ->
                    weatherEntity.toModel()
                }
            }
        }

}