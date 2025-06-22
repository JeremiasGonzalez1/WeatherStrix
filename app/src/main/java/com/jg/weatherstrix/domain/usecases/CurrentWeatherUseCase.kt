package com.jg.weatherstrix.domain.usecases

import com.jg.weatherstrix.domain.interfaces.WeatherRepository
import javax.inject.Inject

class CurrentWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {

    suspend fun invoke(lat:Double, lon:Double) = repository.getWeather(lat = lat, lon = lon)
}