package com.gunishjain.wallpaperapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gunishjain.wallpaperapp.data.models.Photo


@Database(
    entities = [Photo::class],
    version = 1
)
abstract class WallpaperDatabase : RoomDatabase(){

    abstract fun getWallpaperDao() : WallpaperDAO
}