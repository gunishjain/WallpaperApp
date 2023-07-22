package com.gunishjain.wallpaperapp.paging


import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gunishjain.wallpaperapp.data.api.WallpaperAPI
import com.gunishjain.wallpaperapp.data.models.Photo


class WallpaperPagingSource (private val api: WallpaperAPI,
                            private val category :String) :PagingSource<Int,Photo>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
            val currentPage = params.key ?:1
            return try {
                    val response = api.searchBasedOnCategory(category,currentPage)
                        val endOfPaginationReached = response.photos.isEmpty()
                        if(response.photos.isNotEmpty()){
                            LoadResult.Page(
                                data= response.photos,
                                prevKey = if (currentPage==1) null else currentPage-1,
                                nextKey = if(endOfPaginationReached) null else currentPage +1
                            )
                        } else {
                            LoadResult.Page(
                                data = emptyList(),
                                prevKey = null,
                                nextKey = null
                            )
                        }
                    } catch (e: Exception){
                        LoadResult.Error(e)
                    }

    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}