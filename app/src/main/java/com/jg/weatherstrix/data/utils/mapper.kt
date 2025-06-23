package com.jg.weatherstrix.data.utils

import com.jg.weatherstrix.domain.models.Conditions
import com.jg.weatherstrix.domain.models.Weather
import com.jg.weatherstrix.domain.models.Coord
import com.jg.weatherstrix.domain.models.Temperature
import com.jg.weatherstrix.data.database.WeatherEntity
import com.jg.weatherstrix.data.models.WeatherDetailDTO

fun WeatherDetailDTO.toMap():Weather{
    return Weather(
        id = this.id,
        conditions = this.weather.map { item ->
            Conditions(status = item.main, description = item.description
            )},
        city = this.name,
        country = this.sys?.country ?: "Unknown",
        coord = Coord(lat = this.coord.lat, lon = this.coord.lon ),
        temperature = Temperature(
            current = this.main.temp,
            max = this.main.tempMax,
            min = this.main.tempMin,
            feelsLike = this.main.feelsLike ?: this.main.temp
        )
    )
}

fun Weather.toEntity():WeatherEntity{
    return WeatherEntity(
        id = this.id,
        conditions = this.conditions,
        city = this.city,
        country = this.country,
        coord = this.coord,
        temperature = this.temperature
    )
}

fun WeatherEntity.toModel():Weather{
    return Weather(
        id = this.id,
        conditions = this.conditions,
        city = this.city,
        country = this.country,
        coord = this.coord,
        temperature = this.temperature
    )
}