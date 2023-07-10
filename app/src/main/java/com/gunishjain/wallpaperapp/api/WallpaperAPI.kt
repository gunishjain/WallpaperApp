package com.gunishjain.wallpaperapp.api

import com.gunishjain.wallpaperapp.WallpaperResponse
import com.gunishjain.wallpaperapp.util.Constants.Companion.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WallpaperAPI {

    @Headers(
        "Authorization: $API_KEY"
    )
    @GET("v1/search")
    fun searchBasedOnCategory(
        @Query("query")
        searchQuery:String = "nature",
        @Query("page")
        pageNumber:Int = 1,

    ): Call<WallpaperResponse>

}