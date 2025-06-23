package com.jg.weatherstrix.domain.usecases

import com.jg.weatherstrix.domain.interfaces.GetFavoriteRepositoryStorage
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(private val repository: GetFavoriteRepositoryStorage) {
    suspend fun invoke() = repository.getFavorites()
}