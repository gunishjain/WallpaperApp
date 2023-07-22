package com.gunishjain.wallpaperapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.FragmentFullScreenBinding


class FullScreenFragment : Fragment() {

    private var photo: Photo? = null
    private lateinit var binding: FragmentFullScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = requireActivity().window

        // Get the decor view
        val decorView = window.decorView

        // Set the system UI visibility flags
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        decorView.systemUiVisibility = flags
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullScreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWallpaper()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setWallpaper() {
        Glide.with(requireContext())
            .load(photo?.src?.portrait)
            .into(binding.imgWallpaper)
    }

    fun getPhotoData(wallpaper: Photo){
        photo=wallpaper
    }

}