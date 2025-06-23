package com.jg.weatherstrix.data.interfaces

import com.jg.weatherstrix.domain.models.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherDataSourceLocal {
    suspend fun saveWeather(weatherModel: Weather)
    suspend fun deleteWeather(weatherModel: Weather)
    suspend fun getFavorites() : Flow<List<Weather>>
}