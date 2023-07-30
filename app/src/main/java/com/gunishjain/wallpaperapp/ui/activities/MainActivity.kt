package com.gunishjain.wallpaperapp.ui.activities

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.databinding.ActivityMainBinding
import com.gunishjain.wallpaperapp.ui.fragments.CategoryListFragment
import com.gunishjain.wallpaperapp.ui.fragments.FavouriteWallpaperFragment
import com.gunishjain.wallpaperapp.ui.fragments.SearchWallpaperFragment
import com.gunishjain.wallpaperapp.ui.fragments.WallpapersListFragment
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.http.Query

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val fragment = WallpapersListFragment()
    private val categoryFragment = CategoryListFragment()
    private val favouriteFragment = FavouriteWallpaperFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpViews()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, fragment)
        fragmentTransaction.replace(binding.categoryFragment.id, categoryFragment)
        fragmentTransaction.commit()


        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.favourite -> {
                    gotoFavouriteFragment()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                }
                R.id.share ->{
                    Toast.makeText(applicationContext,
                        "Clicked on Shared",Toast.LENGTH_SHORT).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                }
                R.id.rating ->{
                    Toast.makeText(applicationContext,
                        "Clicked on Ratings!",Toast.LENGTH_SHORT).show()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

    }

    override fun onBackPressed() {

        if (binding.mainDrawer.isDrawerOpen(GravityCompat.START)) {
            // Close the navigation drawer
            binding.mainDrawer.closeDrawer(GravityCompat.START);
        } else {
            // Perform the default back button behavior
            super.onBackPressed();
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.toolbar_menu,menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))


                setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        val searchFragment = if (p0.isNullOrEmpty()) {
                            WallpapersListFragment()
                        } else {
                            SearchWallpaperFragment().apply {
                                arguments = Bundle().apply {
                                    putString("query", p0)
                                }
                            }
                        }

                        val fragmentManager = supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(binding.fragmentContainer.id, searchFragment)
                        fragmentTransaction.commit()
                        return true
                    }

                })

            }
        }

        return true
    }


    private fun setUpViews(){
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.appBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.mainDrawer, R.string.open, R.string.close)
        binding.mainDrawer.addDrawerListener(actionBarDrawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle.syncState()

    }

    private fun gotoFavouriteFragment(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, favouriteFragment)
        fragmentTransaction.addToBackStack("favourite_fragment")
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
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