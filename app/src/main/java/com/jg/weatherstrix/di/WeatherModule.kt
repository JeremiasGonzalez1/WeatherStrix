package com.jg.weatherstrix.di

import android.app.Application
import androidx.room.Room
import com.jg.weatherstrix.data.database.WeatherFavoriteDB
import com.jg.weatherstrix.data.datasource.WeatherDataSourceLocalImpl
import com.jg.weatherstrix.data.datasource.WeatherDataSourceRemoteImpl
import com.jg.weatherstrix.data.interfaces.WeatherDataSourceLocal
import com.jg.weatherstrix.data.interfaces.WeatherDataSourceRemote
import com.jg.weatherstrix.data.repository.StorageRepositoryImpl
import com.jg.weatherstrix.data.repository.WeatherRepositoryImpl
import com.jg.weatherstrix.data.network.BaseClient
import com.jg.weatherstrix.domain.interfaces.DeleteFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.interfaces.GetFavoriteRepositoryStorage
import com.jg.weatherstrix.domain.interfaces.SetFavoriteRepositoryStorage
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

    //Data Layer
    @Provides
    @Singleton
    fun provideFavouriteDatabase(app: Application): WeatherFavoriteDB {
        return Room.databaseBuilder(
            app,
            WeatherFavoriteDB::class.java,
            WeatherFavoriteDB.DB_NAME
        ).build()
    }


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
    fun providesWeatherDataSource(baseClient: BaseClient) : WeatherDataSourceRemote =
        WeatherDataSourceRemoteImpl(baseClient)

    @Provides
    @Singleton
    fun provideWeatherDataSourceLocal(db: WeatherFavoriteDB)
            : WeatherDataSourceLocal = WeatherDataSourceLocalImpl(db.favouriteWeatherDAO)

    // Storage Repository Providers
    @Provides
    @Singleton
    fun provideStorageRepository(dataSourceLocal: WeatherDataSourceLocal)
            : StorageRepositoryImpl = StorageRepositoryImpl(dataSourceLocal)

    @Provides
    @Singleton
    fun provideSetFavoriteRepositoryStorage(storageRepository: StorageRepositoryImpl)
            : SetFavoriteRepositoryStorage = storageRepository

    @Provides
    @Singleton
    fun provideGetFavoriteRepositoryStorage(storageRepository: StorageRepositoryImpl)
            : GetFavoriteRepositoryStorage = storageRepository

    @Provides
    @Singleton
    fun provideDeleteFavoriteRepositoryStorage(storageRepository: StorageRepositoryImpl)
            : DeleteFavoriteRepositoryStorage = storageRepository

    @Provides
    @Singleton
    fun providesWeatherRepository(dataSource: WeatherDataSourceRemote) : WeatherRepository =
        WeatherRepositoryImpl(dataSource)

}
