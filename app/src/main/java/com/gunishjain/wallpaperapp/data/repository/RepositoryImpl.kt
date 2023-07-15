package com.gunishjain.wallpaperapp.data.repository

import android.app.Application
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import retrofit2.Call
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: WallpaperAPI,
    private val appContext: Application
) : Repository {

    override suspend fun searchBasedOnCategory(category: String): WallpaperResponse {
        return api.searchBasedOnCategory(category)
    }

    override fun getApplicationContext(): Application {
        return appContext
    }
}