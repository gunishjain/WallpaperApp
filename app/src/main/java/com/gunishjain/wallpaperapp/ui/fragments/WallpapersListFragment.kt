package com.gunishjain.wallpaperapp.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.ui.adapters.WallpaperListAdapter


import com.gunishjain.wallpaperapp.databinding.FragmentWallpapersListBinding
import com.gunishjain.wallpaperapp.ui.activities.WallpaperViewPagerActivity
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WallpapersListFragment : Fragment() {

    private lateinit var binding: FragmentWallpapersListBinding
    private val wallpaperListVM: WallPaperListViewModel by viewModels()
    private lateinit var wallpaperListAdapter: WallpaperListAdapter
    private var currentCategory: String = "nature"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaperListAdapter = WallpaperListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWallpapersListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createWallPaperListRV()
        observeWallPaperList()

        val category = arguments?.getString("category")
        if (!category.isNullOrEmpty()) {
            currentCategory = category
        }

        // Fetch wallpapers based on the current category
        wallpaperListVM.getWallPaperList(currentCategory)

        onWallpaperClick()

    }

    private fun onWallpaperClick() {
        wallpaperListAdapter.setOnItemClickListener { photo->
            val intent = Intent(requireContext(),WallpaperViewPagerActivity::class.java)
            intent.putExtra("photo",photo)
            intent.putExtra("category",currentCategory)
            startActivity(intent)
        }
    }

    private fun createWallPaperListRV() {
        binding.rvWallPapers.apply {
            layoutManager = GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
            adapter=wallpaperListAdapter
        }
    }

    private fun observeWallPaperList() {
        wallpaperListVM.observeWallpaperListLiveData().observe(viewLifecycleOwner
        ) { photosList->
            if(!photosList.isNullOrEmpty()){
                wallpaperListAdapter.setWallpaperList(photosList as ArrayList<Photo>)
            } else {
                Log.d("wplistfragment: ",photosList.size.toString())
            }
        }
    }

    fun updateCategory(category: String) {
        currentCategory = category
        wallpaperListVM.getWallPaperList(currentCategory)
    }

}





