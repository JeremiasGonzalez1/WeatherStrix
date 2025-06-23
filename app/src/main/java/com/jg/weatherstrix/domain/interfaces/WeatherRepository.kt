package com.jg.weatherstrix.domain.interfaces

import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.domain.models.StatusResult

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): StatusResult<Weather>
}