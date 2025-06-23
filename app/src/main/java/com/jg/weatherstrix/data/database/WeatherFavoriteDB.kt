package com.jg.weatherstrix.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

private const val VERSION_ACCESS_DB = 1

@Database(
    version = VERSION_ACCESS_DB,
    entities = [WeatherEntity::class],
    exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherFavoriteDB : RoomDatabase() {
    companion object {
        const val DB_NAME = "favouriteWeather_database"
    }
    abstract val favouriteWeatherDAO: WeatherFavoriteDao
}