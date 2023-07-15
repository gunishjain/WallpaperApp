package com.gunishjain.wallpaperapp.data.repository

import android.app.Application
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import retrofit2.Call

interface Repository {

    suspend fun searchBasedOnCategory(category:String) : WallpaperResponse
    fun getApplicationContext() : Application

}