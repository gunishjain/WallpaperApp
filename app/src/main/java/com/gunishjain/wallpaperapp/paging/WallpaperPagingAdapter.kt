package com.gunishjain.wallpaperapp.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.WpSmallBinding


class WallpaperPagingAdapter: PagingDataAdapter<Photo,WallpaperPagingAdapter.WallpaperListViewHolder>(
    COMPARATOR) {

    private var itemWidth: Int = 0
    private var itemHeight: Int = 0
    private var onItemClick: ((Photo) -> Unit)? = null

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClick = listener
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

        return WallpaperListViewHolder(
            WpSmallBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WallpaperListViewHolder, position: Int) {

        val item = getItem(position)
        val layoutParams = holder.binding.imgWallpaper.layoutParams
        layoutParams.width = itemWidth
        layoutParams.height = itemHeight
        holder.binding.imgWallpaper.layoutParams = layoutParams

        Glide.with(holder.itemView)
            .load(item!!.src.portrait)
            .into(holder.binding.imgWallpaper)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    class WallpaperListViewHolder(var binding: WpSmallBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return  oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem==newItem
            }
        }
    }




}