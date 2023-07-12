package com.gunishjain.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.Photo
import com.gunishjain.wallpaperapp.databinding.SingleWallpaperViewBinding

class SingleWallpaperAdapter(private val wallpaperList: ArrayList<Photo>,private val viewPager2: ViewPager2)
    :RecyclerView.Adapter<SingleWallpaperAdapter.SingleImageViewHolder>() {

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

        Glide.with(holder.itemView)
            .load(wallpaperList[position].src.portrait)
            .into(holder.binding.imgSingleWallpaper)

        if(position==wallpaperList.size-1){
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        wallpaperList.addAll(wallpaperList)
        notifyDataSetChanged()
    }

}