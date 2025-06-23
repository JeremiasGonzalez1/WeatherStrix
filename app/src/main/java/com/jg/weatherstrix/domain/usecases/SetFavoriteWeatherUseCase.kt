package com.jg.weatherstrix.domain.usecases

import com.jg.weatherstrix.domain.interfaces.SetFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.models.Weather
import javax.inject.Inject

class SetFavoriteWeatherUseCase @Inject constructor(
    private val repository: SetFavoriteRepositoryStorage
) {
    suspend operator fun invoke(weather: Weather) {
        repository.setFavorite(weather)
    }
}