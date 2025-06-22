package com.jg.weatherstrix.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

import io.ktor.utils.io.errors.IOException
import javax.inject.Inject

class BaseClient @Inject constructor(private val httpClient : HttpClient){

    suspend fun localWeather(lat:Double, lon:Double):HttpStatus{
        return try {
            val result = httpClient.get(BaseUrl.BASE_URL + "weather") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("appid", "581d9978b7476c4c7d268a7cad184fec")}
            if (result.status.value in 200.. 299){
                HttpStatus(httpResponse =  result)
            }else{
                HttpStatus(errorMessage = "fallo")
            }

        }catch (e: HttpRequestTimeoutException){
            HttpStatus(errorMessage = e.message ?: "fail")
        }catch (e: RedirectResponseException) {
            println("redirect: ${e.response.status}")
            HttpStatus(errorMessage = e.message)

        } catch (e: ClientRequestException) {
            println("error: ${e.response.status}")
            HttpStatus(errorMessage = e.message)

        } catch (e: ServerResponseException) {
            println("error: ${e.response.status}")
            HttpStatus(errorMessage = e.message)

        } catch (e: IOException) {
            println("IO error")
            HttpStatus(errorMessage = e.message ?: "fail")
        }
    }
}

data class HttpStatus(
    val httpResponse: HttpResponse? = null,
    val errorMessage: String = ""
)
