package com.jg.weatherstrix.data.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jg.weatherstrix.domain.models.Conditions
import com.jg.weatherstrix.domain.models.Coord
import com.jg.weatherstrix.domain.models.Temperature

@Entity
data class WeatherEntity(
    @PrimaryKey
    val id:Int,
    val conditions: List<Conditions>,
    val country: String,
    val city: String,
    val coord: Coord,
    val temperature: Temperature
)