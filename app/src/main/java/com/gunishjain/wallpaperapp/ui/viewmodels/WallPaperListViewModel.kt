package com.gunishjain.wallpaperapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.data.models.WallpaperResponse
import com.gunishjain.wallpaperapp.data.api.RetrofitInstance
import com.gunishjain.wallpaperapp.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class WallPaperListViewModel @Inject constructor(
    private val repository: Repository
):ViewModel() {

    private var wallpaperListLiveData = MutableLiveData<List<Photo>>()

    fun getWallPaperList(category: String){

        viewModelScope.launch {
            try {
                val wallpaperResponse = repository.searchBasedOnCategory(category)
                val photos = wallpaperResponse.photos
                wallpaperListLiveData.value = photos
            } catch (e: Exception){
                Log.d("Wallpaperlsitvm",e.toString())
                wallpaperListLiveData.value = emptyList()
            }
        }

    }

    fun observeWallpaperListLiveData() : LiveData<List<Photo>>{
        return wallpaperListLiveData
    }

}