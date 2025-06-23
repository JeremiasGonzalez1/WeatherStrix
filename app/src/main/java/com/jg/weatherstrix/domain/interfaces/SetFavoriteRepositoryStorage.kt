package com.jg.weatherstrix.domain.interfaces

import com.jg.weatherstrix.domain.models.Weather

interface SetFavoriteRepositoryStorage {
    suspend fun setFavorite(weather: Weather)
}