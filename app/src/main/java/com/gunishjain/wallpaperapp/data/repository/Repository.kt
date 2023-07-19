package com.gunishjain.wallpaperapp.data.repository

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.db.WallpaperDAO
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import java.io.File
import java.io.FileOutputStream
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


    fun downloadWallpaper(wallpaper : Photo){
        val wallpaperBitmap = Glide.with(appContext)
            .asBitmap()
            .load(wallpaper.src.portrait)
            .submit()
            .get()

//        val sourceWidth: Int = wallpaper.width
//        val sourceHeight: Int = wallpaper.height
//
//        var targetWidth = 1080
//        var targetHeight = 2400
//
//        val sourceRatio = sourceWidth.toFloat() / sourceHeight.toFloat()
//        val targetRatio = targetWidth.toFloat() / targetHeight.toFloat()
//
//        if (targetRatio > sourceRatio) {
//            targetWidth = (targetHeight.toFloat() * sourceRatio).toInt()
//        } else {
//            targetHeight = (targetWidth.toFloat() / sourceRatio).toInt()
//        }

//        val resizedBitmap=Bitmap.createScaledBitmap(wallpaperBitmap,targetWidth,targetHeight,true)

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