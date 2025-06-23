package com.jg.weatherstrix.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherFavoriteDao {
    @Query("SELECT * FROM WeatherEntity")
    fun getWeathers(): Flow<List<WeatherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(weatherEntity: WeatherEntity)

    @Delete
    fun delete(weatherEntity: WeatherEntity)
}