package com.jg.weatherstrix.domain.interfaces

import com.jg.weatherstrix.domain.models.Weather

interface DeleteFavoriteRepositoryStorage {
    suspend fun deleteFavorite(weather: Weather)
}