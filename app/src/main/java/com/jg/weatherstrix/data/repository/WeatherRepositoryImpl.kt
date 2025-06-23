package com.jg.weatherstrix.data.repository

import com.jg.weatherstrix.data.interfaces.WeatherDataSourceRemote
import com.jg.weatherstrix.data.utils.toMap
import com.jg.weatherstrix.domain.interfaces.WeatherRepository
import com.jg.weatherstrix.domain.models.StatusResult
import com.jg.weatherstrix.domain.models.Weather
import javax.inject.Inject

class WeatherRepositoryImpl  @Inject constructor(private val dataSource: WeatherDataSourceRemote):
    WeatherRepository {
    override suspend fun getWeather(lat:Double, lon:Double): StatusResult<Weather> {
        return when(val response = dataSource.getWeather(lat,lon)){
            is StatusResult.Error -> StatusResult.Error(response.message)
            is StatusResult.Success -> {
                StatusResult.Success(response.value.toMap())
            }
        }
    }
}

