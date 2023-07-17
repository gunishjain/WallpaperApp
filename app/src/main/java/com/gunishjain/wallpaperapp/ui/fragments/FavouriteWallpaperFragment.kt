package com.gunishjain.wallpaperapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.FragmentFavouriteWallpaperBinding
import com.gunishjain.wallpaperapp.databinding.FragmentWallpapersListBinding
import com.gunishjain.wallpaperapp.ui.adapters.WallpaperListAdapter
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavouriteWallpaperFragment : Fragment() {


    private lateinit var binding: FragmentFavouriteWallpaperBinding
    private val wallpaperListVM: WallPaperListViewModel by viewModels()
    private lateinit var wallpaperListAdapter: WallpaperListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaperListAdapter = WallpaperListAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteWallpaperBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createFavWallPaperRV()
        observeFavWallPaperList()
        wallpaperListVM.getSavedWallpapers()
    }

    private fun createFavWallPaperRV() {
        binding.rvWallPapers.apply {
            layoutManager = GridLayoutManager(activity,3, GridLayoutManager.VERTICAL,false)
            adapter=wallpaperListAdapter
        }
    }

    private fun observeFavWallPaperList() {
        wallpaperListVM.observeFavWallpapersListLiveData().observe(viewLifecycleOwner) { favPhotos ->
            wallpaperListAdapter.setWallpaperList(favPhotos as ArrayList<Photo>)
        }
    }

}