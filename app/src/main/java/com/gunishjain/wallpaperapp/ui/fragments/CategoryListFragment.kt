package com.gunishjain.wallpaperapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.databinding.FragmentCategoryListBinding
import com.gunishjain.wallpaperapp.ui.activities.MainActivity
import com.gunishjain.wallpaperapp.ui.adapters.CategoryListAdapter
import dagger.hilt.android.AndroidEntryPoint

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var categoryListAdapter: CategoryListAdapter
    private val fragment = WallpapersListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryListAdapter= CategoryListAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createCategoryListRV()
        populateCategories()
        onCategoryClick()

    }

    private fun populateCategories() {
        val categoryList: ArrayList<String> = getCategoryList()
        categoryListAdapter.setCategoryList(categoryList = categoryList)
    }

    private fun createCategoryListRV() {
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter=categoryListAdapter
        }
    }


    private fun getCategoryList(): ArrayList<String> {
        return arrayListOf(
            "All",
            "Nature",
            "Beach",
            "Space",
            "Sky",
            "Food",
            "Retro",
            "Cars",
        )
    }

    private fun onCategoryClick() {
        categoryListAdapter.setOnItemClickListener { category ->
//            fragment.updateCategory(category)
            if (activity is MainActivity) {
                val mainActivity = activity as MainActivity
                mainActivity.updateCategory(category)
            }

        }
    }

}