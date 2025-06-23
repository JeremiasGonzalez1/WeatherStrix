package com.jg.weatherstrix.data.database

import com.jg.weatherstrix.domain.models.Coord
import com.jg.weatherstrix.domain.models.Temperature
import com.jg.weatherstrix.domain.models.Conditions
import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private object JsonParser {
    val json = Json { ignoreUnknownKeys = true }
}
class Converters {

    @TypeConverter
    fun fromConditions(value: List<Conditions>): String =
        JsonParser.json.encodeToString(value)

    @TypeConverter
    fun toConditions(value: String): List<Conditions> =
        JsonParser.json.decodeFromString(value)

    @TypeConverter
    fun fromCoord(value: Coord): String =
        JsonParser.json.encodeToString(value)

    @TypeConverter
    fun toCoord(value: String): Coord =
        JsonParser.json.decodeFromString(value)

    @TypeConverter
    fun fromTemperature(value: Temperature): String =
        JsonParser.json.encodeToString(value)

    @TypeConverter
    fun toTemperature(value: String): Temperature =
        JsonParser.json.decodeFromString(value)
}