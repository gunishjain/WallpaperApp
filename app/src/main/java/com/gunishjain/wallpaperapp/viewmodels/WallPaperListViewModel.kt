package com.gunishjain.wallpaperapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gunishjain.wallpaperapp.Photo
import com.gunishjain.wallpaperapp.WallpaperResponse
import com.gunishjain.wallpaperapp.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WallPaperListViewModel():ViewModel() {

    private var wallpaperListLiveData = MutableLiveData<List<Photo>>()

    fun getWallPaperList(category: String){
        RetrofitInstance.api.searchBasedOnCategory(category).enqueue(object: Callback<WallpaperResponse>{
            override fun onResponse(
                call: Call<WallpaperResponse>,
                response: Response<WallpaperResponse>
            ) {
                if(response.body()!=null){
                    val wallpapers: List<Photo> = response.body()!!.photos
                    wallpaperListLiveData.value=wallpapers
                }
            }
            override fun onFailure(call: Call<WallpaperResponse>, t: Throwable) {
                Log.d("WallpaperListViewModel",t.message.toString())
            }

        })
    }

    fun observeWallpaperListLiveData() : LiveData<List<Photo>>{
        return wallpaperListLiveData
    }


}