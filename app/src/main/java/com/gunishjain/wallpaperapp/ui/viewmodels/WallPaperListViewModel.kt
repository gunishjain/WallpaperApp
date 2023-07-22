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
class WallPaperListViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {

    private var wallpaperListLiveData = MutableLiveData<List<Photo>>()
    private var favWallpaperLiveData = MutableLiveData<List<Photo>>()

    fun getWallPaperList(category: String){

        viewModelScope.launch {
            try {
                val wallpaperResponse = repository.searchBasedOnCategory(category)
                val photos = wallpaperResponse.photos
                wallpaperListLiveData.value = photos
                Log.d("Wplistvm",photos.toString())
            } catch (e: Exception){
                Log.d("Wplistvm",e.toString())
                wallpaperListLiveData.value = emptyList()
            }
        }

    }

    fun getSavedWallpapers() {
        viewModelScope.launch {
            val savedWallpapers = repository.getWallpapers()
            favWallpaperLiveData.postValue(savedWallpapers)
        }
    }

    fun observeWallpaperListLiveData() : LiveData<List<Photo>>{
        return wallpaperListLiveData
    }

    fun observeFavWallpapersListLiveData() : LiveData<List<Photo>>{
        return favWallpaperLiveData
    }

}