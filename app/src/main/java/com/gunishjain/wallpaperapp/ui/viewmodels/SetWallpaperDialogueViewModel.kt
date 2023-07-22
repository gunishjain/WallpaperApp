package com.gunishjain.wallpaperapp.ui.viewmodels

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.gunishjain.wallpaperapp.data.repository.Repository
import com.gunishjain.wallpaperapp.data.models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SetWallpaperDialogueViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val context = repository.getApplicationContext()
    private val wallpaperManager= WallpaperManager.getInstance(context)

    fun setWallPaper(wpFlag: Int,wallpaper: Photo){

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val wallpaperBitmap = Glide.with(context)
                    .asBitmap()
                    .load(wallpaper.src.portrait)
                    .submit()
                    .get()

                when (wpFlag) {
                    1 -> wallpaperManager.setBitmap(
                        wallpaperBitmap, null, false, WallpaperManager.FLAG_SYSTEM
                    )
                    2 -> wallpaperManager.setBitmap(
                        wallpaperBitmap, null, false, WallpaperManager.FLAG_LOCK
                    )
                    3 -> wallpaperManager.setBitmap(
                        wallpaperBitmap,
                        null,
                        false,
                        WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
                    )
                }
            } catch (e: Exception){
                Log.d("setwallpaperdialoguevm",e.toString())
            }
        }
    }

}

