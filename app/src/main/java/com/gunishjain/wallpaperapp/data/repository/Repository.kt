package com.gunishjain.wallpaperapp.data.repository

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.db.WallpaperDAO
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import com.gunishjain.wallpaperapp.paging.WallpaperPagingSource
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: WallpaperAPI,
    private val appContext: Application,
    private val wallpaperDao: WallpaperDAO
) {

     suspend fun searchBasedOnCategory(category: String,pageNumber:Int): WallpaperResponse {
        return api.searchBasedOnCategory(category,pageNumber)
    }

    fun searchPagination(category: String): Flow<PagingData<Photo>> {
        return Pager(
            config= PagingConfig(pageSize = 20 ),
            pagingSourceFactory = {
                WallpaperPagingSource(api,category)
            }
        ).flow
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


    fun downloadWallpaper(wallpaper : Photo){
        val wallpaperBitmap = Glide.with(appContext)
            .asBitmap()
            .load(wallpaper.src.portrait)
            .submit()
            .get()

        val fileName = "${System.currentTimeMillis()}.jpg"
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName)

        // Save the image to the file
        val outputStream = FileOutputStream(file)
        wallpaperBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        val uri = Uri.fromFile(file)

        // Send a broadcast to the system
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = uri
        appContext.sendBroadcast(intent)

    }

}