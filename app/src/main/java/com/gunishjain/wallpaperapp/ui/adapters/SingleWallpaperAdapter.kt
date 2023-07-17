package com.gunishjain.wallpaperapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.SingleWallpaperViewBinding
import com.gunishjain.wallpaperapp.ui.fragments.SetWallpaperDialogue

class SingleWallpaperAdapter() :RecyclerView.Adapter<SingleWallpaperAdapter.SingleImageViewHolder>() {

    private var wallpaperList = ArrayList<Photo>()
    private lateinit var viewPager2: ViewPager2
    private var onItemClick: ((Photo) -> Unit)? = null

    fun setWallpaperViewPager(wallpaperList : ArrayList<Photo>, viewPager2: ViewPager2){
        this.wallpaperList=wallpaperList
        this.viewPager2=viewPager2
        notifyDataSetChanged()

    }

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClick = listener
    }

    class SingleImageViewHolder(val binding:SingleWallpaperViewBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleImageViewHolder {
        return SingleImageViewHolder(
            SingleWallpaperViewBinding.inflate(LayoutInflater.from(parent.context),
                parent,false))
    }

    override fun getItemCount(): Int {
        return wallpaperList.size
    }

    override fun onBindViewHolder(holder: SingleImageViewHolder, position: Int) {

        val wallpaper = wallpaperList[position]

        Glide.with(holder.itemView)
            .load(wallpaper.src.portrait)
            .into(holder.binding.imgSingleWallpaper)

        val colorResId = if (wallpaper.liked) R.color.red else R.color.white
        holder.binding.imgFavourite.setColorFilter(ContextCompat.getColor(holder.itemView.context, colorResId))

        if(position==wallpaperList.size-1){
            viewPager2.post(runnable)
        }

        holder.binding.imgSetWallPaper.setOnClickListener {
            onItemClick?.invoke(wallpaper)
        }

        holder.binding.imgFavourite.setOnClickListener{
            onItemClick?.invoke(wallpaper)
        }

    }

    private val runnable = Runnable {
        wallpaperList.addAll(wallpaperList)
        notifyDataSetChanged()
    }

}