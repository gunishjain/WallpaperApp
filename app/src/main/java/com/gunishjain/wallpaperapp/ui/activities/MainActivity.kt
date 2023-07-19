package com.gunishjain.wallpaperapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.ui.adapters.CategoryListAdapter
import com.gunishjain.wallpaperapp.databinding.ActivityMainBinding
import com.gunishjain.wallpaperapp.ui.fragments.CategoryListFragment
import com.gunishjain.wallpaperapp.ui.fragments.FavouriteWallpaperFragment
import com.gunishjain.wallpaperapp.ui.fragments.WallpapersListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private val fragment = WallpapersListFragment()
    private val categoryFragment = CategoryListFragment()
    private val favouriteFragment = FavouriteWallpaperFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, fragment)
        fragmentTransaction.replace(binding.categoryFragment.id, categoryFragment)
        fragmentTransaction.commit()


        toggle = ActionBarDrawerToggle(this,binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.favourite -> gotoFavouriteFragment()

                R.id.categories ->Toast.makeText(applicationContext,
                    "Clicked on Category",Toast.LENGTH_SHORT).show()

                R.id.share ->Toast.makeText(applicationContext,
                    "Clicked on Shared",Toast.LENGTH_SHORT).show()

                R.id.rating ->Toast.makeText(applicationContext,
                    "Clicked on Ratings!",Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    private fun gotoFavouriteFragment(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, favouriteFragment)
        fragmentTransaction.addToBackStack("favourite_fragment")
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateCategory(category: String) {

        val wallpapersListFragment = WallpapersListFragment()
        wallpapersListFragment.arguments = Bundle().apply {
            putString("category", category)
        }
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id, wallpapersListFragment)
        transaction.commit()
    }


}