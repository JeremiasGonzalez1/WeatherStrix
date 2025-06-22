package com.jg.weatherstrix.data.interfaces

import com.jg.weatherstrix.data.models.WeatherDetailDTO
import com.jg.weatherstrix.domain.models.StatusResult

interface WeatherDataSourceRemote {
    suspend fun getWeather(lat:Double, lon:Double): StatusResult<WeatherDetailDTO>
}