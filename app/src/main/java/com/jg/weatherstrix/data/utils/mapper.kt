package com.jg.weatherstrix.data.utils

import Conditions
import Weather
import Coord
import com.jg.weatherstrix.data.models.WeatherDTO

fun WeatherDTO.toMap():Weather{
    return Weather(
        conditions = this.weather.map { item ->
            Conditions(status = item.main, description = item.description
            )},
        city = this.name,
        country = this.sys.country,
        coord = Coord(lat = this.coord.lat, lon = this.coord.lon )
    )
}