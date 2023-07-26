package com.gunishjain.wallpaperapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.databinding.FragmentSearchWallpaperBinding
import com.gunishjain.wallpaperapp.databinding.FragmentWallpapersListBinding
import com.gunishjain.wallpaperapp.paging.WallpaperPagingAdapter
import com.gunishjain.wallpaperapp.ui.activities.WallpaperViewPagerActivity
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchWallpaperFragment : Fragment() {

    private lateinit var binding: FragmentSearchWallpaperBinding
    private val wallpaperListVM: WallPaperListViewModel by viewModels()
    private lateinit var wallpaperPagingAdapter : WallpaperPagingAdapter

    private var currentSearchQuery: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wallpaperPagingAdapter= WallpaperPagingAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchWallpaperBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createWallPaperListRV()

        val query = arguments?.getString("query")
        if (!query.isNullOrEmpty()) {
            currentSearchQuery = query
            wallpaperListVM.updateSearchQuery(query)
            wallpaperListVM.getWallpapersPaginated(query)
            observeWallPaperList()

        }
    }

    private fun observeWallPaperList() {
        lifecycleScope.launch {
            wallpaperListVM.paginatedWallpapers.collectLatest {
                wallpaperPagingAdapter.submitData(it)
            }
        }

    }

    private fun createWallPaperListRV() {
        binding.rvSearchWallPapers.apply {
            layoutManager = GridLayoutManager(activity,3, GridLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter=wallpaperPagingAdapter
        }
    }

}