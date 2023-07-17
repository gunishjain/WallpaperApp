package com.gunishjain.wallpaperapp.ui.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewPagerViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private var savedWallpapersLiveData = MutableLiveData<List<Photo>>()
    private var wallpaperLiveData = MutableLiveData<Photo>()

    fun insertWallpaper(wallpaper: Photo) = viewModelScope.launch {
        repository.insertWallpaper(wallpaper)
    }

    fun deleteWallpaper(wallpaper: Photo) = viewModelScope.launch {
        repository.deleteWallpaper(wallpaper)
    }

    fun getWallpaperById(id: Int) = viewModelScope.launch {
        val wallpaper = repository.getWallpaperById(id)
        wallpaperLiveData.value= wallpaper.value
    }

    fun getSavedWallpapers() {
        viewModelScope.launch {
            val savedWallpapers = repository.getWallpapers()

            Log.d("wallpaperVPVM-lol",savedWallpapers.toString())
            savedWallpapersLiveData.value=savedWallpapers
        }
    }


    fun observeSavedWallpapers() : LiveData<List<Photo>>{
        return savedWallpapersLiveData
    }

    fun observeWallpaperById() : LiveData<Photo> {
        return wallpaperLiveData
    }

}