package com.gunishjain.wallpaperapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gunishjain.wallpaperapp.data.models.Photo

@Dao
interface WallpaperDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: Photo)

    @Delete
    suspend fun deleteWallpaper(wallpaper: Photo)

    @Query("SELECT * FROM wallpapers_table ORDER BY id DESC")
    suspend fun getWallpapers():List<Photo>

    @Query("SELECT * FROM wallpapers_table WHERE id=:id")
    fun getWallpaperById(id: Int) : LiveData<Photo>


}