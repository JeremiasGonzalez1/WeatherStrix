package com.jg.weatherstrix.domain.interfaces

import com.jg.weatherstrix.domain.models.Weather
import kotlinx.coroutines.flow.Flow

interface GetFavoriteRepositoryStorage {
    suspend fun getFavorites(): Flow<List<Weather>>
}