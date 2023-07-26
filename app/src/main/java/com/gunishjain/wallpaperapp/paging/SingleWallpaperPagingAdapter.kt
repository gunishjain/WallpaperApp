package com.gunishjain.wallpaperapp.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.SingleWallpaperViewBinding


class SingleWallpaperPagingAdapter:
    PagingDataAdapter<Photo,SingleWallpaperPagingAdapter.SingleWallpaperViewHolder>(COMPARATOR) {

    private lateinit var viewPager2: ViewPager2
    private var onFavItemClick: ((Photo) -> Unit)? = null
    private var onSetWPItemClick: ((Photo) -> Unit)? = null
    private var onWallpaperItemClick: ((Photo) -> Unit)? = null
    private var onSetDownloadItemClick: ((Photo) -> Unit)? = null

    fun setWallpaperViewPager(viewPager2: ViewPager2){
        this.viewPager2=viewPager2
        notifyDataSetChanged()

    }

    fun setOnFavItemClickListener(listener: (Photo) -> Unit) {
        onFavItemClick = listener
    }

    fun setOnWPItemClickListener(listener: (Photo) -> Unit) {
        onSetWPItemClick = listener
    }

    fun setOnDownloadClickListener(listener: (Photo) -> Unit){
        onSetDownloadItemClick=listener
    }

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onWallpaperItemClick = listener
    }



    override fun onBindViewHolder(holder: SingleWallpaperViewHolder, position: Int) {
        val wallpaper = getItem(position)

//        val wallpaper = wallpaperList[position]

        Glide.with(holder.itemView)
            .load(wallpaper!!.src.portrait)
            .into(holder.binding.imgSingleWallpaper)

        val colorResId = if (wallpaper.liked) R.color.red else R.color.white
        holder.binding.imgFavourite.setColorFilter(ContextCompat.getColor(holder.itemView.context, colorResId))


        holder.binding.imgSingleWallpaper.setOnClickListener {
            onWallpaperItemClick?.invoke(wallpaper)
        }

        holder.binding.imgSetWallPaper.setOnClickListener {
            onSetWPItemClick?.invoke(wallpaper)
        }

        holder.binding.imgFavourite.setOnClickListener{
            onFavItemClick?.invoke(wallpaper)
        }

        holder.binding.imgDownload.setOnClickListener {
            onSetDownloadItemClick?.invoke(wallpaper)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleWallpaperViewHolder {
        return SingleWallpaperViewHolder(
            SingleWallpaperViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ))

    }

    class SingleWallpaperViewHolder(var binding: SingleWallpaperViewBinding): RecyclerView.ViewHolder(binding.root)


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