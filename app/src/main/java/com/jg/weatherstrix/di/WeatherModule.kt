package com.jg.weatherstrix.di

import com.jg.weatherstrix.data.datasource.WeatherDataSourceRemoteImpl
import com.jg.weatherstrix.data.interfaces.WeatherDataSourceRemote
import com.jg.weatherstrix.data.repository.WeatherRepositoryImpl
import com.jg.weatherstrix.data.utils.BaseClient
import com.jg.weatherstrix.domain.interfaces.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    fun providesClient() =  HttpClient(CIO) {
        Json { ignoreUnknownKeys = true }
        install(ContentNegotiation) {
            json(
                json = Json { ignoreUnknownKeys = true }
            )
        }
    }

    @Provides
    @Singleton
    fun providesBaseClient(httpClient: HttpClient) : BaseClient = BaseClient(httpClient)

    @Provides
    @Singleton
    fun  providesWeatherDataSource(baseClient: BaseClient) : WeatherDataSourceRemote =
        WeatherDataSourceRemoteImpl(baseClient)

    @Provides
    @Singleton
    fun providesWeatherRepository(dataSource: WeatherDataSourceRemoteImpl) : WeatherRepository =
        WeatherRepositoryImpl(dataSource)

}