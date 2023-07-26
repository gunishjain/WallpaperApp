package com.gunishjain.wallpaperapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.repository.Repository
import com.gunishjain.wallpaperapp.paging.WallpaperPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WallPaperListViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {

    private var wallpaperListLiveData = MutableLiveData<List<Photo>>()
    private var favWallpaperLiveData = MutableLiveData<List<Photo>>()
    private val _paginatedWallpapers = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val paginatedWallpapers= _paginatedWallpapers
    private var searchQuery: String = ""

    fun updateSearchQuery(newText: String?) {
        searchQuery = newText ?: ""
    }

    fun getWallPaperList(category: String){

        viewModelScope.launch {
            try {
                val wallpaperResponse = repository.searchBasedOnCategory(category,1)
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


    fun getWallpapersPaginated(category: String){
        viewModelScope.launch {
            repository.searchPagination(category).cachedIn(viewModelScope).collect{
                _paginatedWallpapers.value=it
            }
        }
    }

    fun observeWallpaperListLiveData() : LiveData<List<Photo>>{
        return wallpaperListLiveData
    }

    fun observeFavWallpapersListLiveData() : LiveData<List<Photo>>{
        return favWallpaperLiveData
    }


}