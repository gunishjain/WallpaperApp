package com.gunishjain.wallpaperapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.gunishjain.wallpaperapp.adapters.CategoryListAdapter
import com.gunishjain.wallpaperapp.adapters.WallpaperListAdapter
import com.gunishjain.wallpaperapp.databinding.ActivityMainBinding
import com.gunishjain.wallpaperapp.ui.fragments.WallpapersListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var categoryListAdapter: CategoryListAdapter
    private val fragment = WallpapersListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        categoryListAdapter= CategoryListAdapter()

        createCategoryListRV()
        populateCategories()
        onCategoryClick()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, fragment)
        fragmentTransaction.commit()

        toggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.favourite-> Toast.makeText(applicationContext,
                    "Clicked on Fav",Toast.LENGTH_SHORT).show()

                R.id.categories->Toast.makeText(applicationContext,
                    "Clicked on Category",Toast.LENGTH_SHORT).show()

                R.id.share->Toast.makeText(applicationContext,
                    "Clicked on Shared",Toast.LENGTH_SHORT).show()

                R.id.rating->Toast.makeText(applicationContext,
                    "Clicked on Ratings!",Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun onCategoryClick() {
        categoryListAdapter.setOnItemClickListener { category ->
            fragment.updateCategory(category)
        }
    }

    private fun populateCategories() {
        val categoryList: ArrayList<String> = getCategoryList()
        categoryListAdapter.setCategoryList(categoryList = categoryList)
    }

    private fun createCategoryListRV() {
        binding.rvCategoryList.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            adapter=categoryListAdapter
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCategoryList(): ArrayList<String> {
        return arrayListOf(
            "Nature",
            "Beach",
            "Space",
            "Sky",
            "Food",
            "Retro",
            "Cars",
        )
    }


}