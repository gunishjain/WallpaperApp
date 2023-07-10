package com.gunishjain.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.Photo
import com.gunishjain.wallpaperapp.Src
import com.gunishjain.wallpaperapp.databinding.WpSmallBinding

class WallpaperListAdapter(): RecyclerView.Adapter<WallpaperListAdapter.WallpaperListViewHolder>() {

    private var wallpaperList = ArrayList<Photo>()
    private var itemWidth: Int = 0
    private var itemHeight: Int = 0

    fun setWallpaperList(wallpaperList: ArrayList<Photo>){
        this.wallpaperList=wallpaperList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperListViewHolder {
        if (itemWidth == 0 || itemHeight == 0) {
            val screenWidth = parent.context.resources.displayMetrics.widthPixels
            val columnCount = 3 // Number of columns in the grid layout
            itemWidth = screenWidth / columnCount

            val originalWidth = 800 // Original width of the image
            val originalHeight = 1200 // Original height of the image

            val scaleFactor = itemWidth.toFloat() / originalWidth.toFloat()
            itemHeight = (originalHeight * scaleFactor).toInt()
        }

        return WallpaperListViewHolder(WpSmallBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }

    override fun onBindViewHolder(holder: WallpaperListViewHolder, position: Int) {


        val layoutParams = holder.binding.imgWallpaper.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemHeight
        holder.binding.imgWallpaper.layoutParams = layoutParams

        Glide.with(holder.itemView)
            .load(wallpaperList[position].src.portrait)
            .into(holder.binding.imgWallpaper)
    }

    class WallpaperListViewHolder(var binding: WpSmallBinding):RecyclerView.ViewHolder(binding.root)

}