package com.jg.weatherstrix.data.utils

import Conditions
import Weather
import Coord
import Temperature
import com.jg.weatherstrix.data.models.WeatherDetailDTO

fun WeatherDetailDTO.toMap():Weather{
    return Weather(
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