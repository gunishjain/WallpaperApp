package com.gunishjain.wallpaperapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.db.WallpaperDatabase
import com.gunishjain.wallpaperapp.data.repository.Repository
import com.gunishjain.wallpaperapp.data.repository.RepositoryImpl
import com.gunishjain.wallpaperapp.util.Constants
import com.gunishjain.wallpaperapp.util.Constants.Companion.WALLPAPAPER_DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesWallpaperApi(): WallpaperAPI {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(WallpaperAPI::class.java)

    }

    @Provides
    @Singleton
    fun provideRepository(api: WallpaperAPI,app: Application) : Repository {
        return RepositoryImpl(api,app)
    }

    @Provides
    @Singleton
    fun provideWallpaperDatabase(
        @ApplicationContext app:Context) = Room.databaseBuilder(
            app,
            WallpaperDatabase::class.java,
            WALLPAPAPER_DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideWallpaperDao(db: WallpaperDatabase)=db.getWallpaperDao()

}