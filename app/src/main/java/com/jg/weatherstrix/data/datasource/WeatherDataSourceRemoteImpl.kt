package com.jg.weatherstrix.data.datasource

import com.jg.weatherstrix.domain.models.StatusResult
import com.jg.weatherstrix.data.interfaces.WeatherDataSourceRemote
import com.jg.weatherstrix.data.models.WeatherDetailDTO
import com.jg.weatherstrix.data.network.BaseClient
import io.ktor.client.call.body
import javax.inject.Inject


class WeatherDataSourceRemoteImpl @Inject constructor(private val baseClient: BaseClient):
    WeatherDataSourceRemote {
    override suspend fun getWeather(lat:Double, lon:Double): StatusResult<WeatherDetailDTO> {
        val response = baseClient.localWeather(lat = lat, lon = lon)
        try {
            response.let {
                if (it.httpResponse != null){
                    return StatusResult.Success(it.httpResponse.body())
                }
            }
            return StatusResult.Error(response.errorMessage)
        }catch (e:Exception){
            return StatusResult.Error(e.message ?: "Fail server")
        }
    }
}