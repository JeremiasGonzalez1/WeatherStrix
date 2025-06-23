package com.jg.weatherstrix.domain.usecases

import com.jg.weatherstrix.domain.interfaces.DeleteFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.models.Weather
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val repository: DeleteFavoriteRepositoryStorage
) {
    suspend operator fun invoke(weather: Weather) {
        repository.deleteFavorite(weather)
    }
}