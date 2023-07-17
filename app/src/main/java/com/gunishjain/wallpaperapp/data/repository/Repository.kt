package com.gunishjain.wallpaperapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.db.WallpaperDAO
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: WallpaperAPI,
    private val appContext: Application,
    private val wallpaperDao: WallpaperDAO
) {

     suspend fun searchBasedOnCategory(category: String): WallpaperResponse {
        return api.searchBasedOnCategory(category)
    }

     fun getApplicationContext(): Application {
        return appContext
    }

     suspend fun insertWallpaper(wallpaper: Photo) {
         wallpaperDao.insertWallpaper(wallpaper)

    }

     suspend fun deleteWallpaper(wallpaper: Photo) {
         wallpaperDao.deleteWallpaper(wallpaper)
    }

     suspend fun getWallpapers(): List<Photo> {
         return wallpaperDao.getWallpapers()
     }

    fun getWallpaperById(id: Int) : LiveData<Photo> {
        return wallpaperDao.getWallpaperById(id)
    }

}