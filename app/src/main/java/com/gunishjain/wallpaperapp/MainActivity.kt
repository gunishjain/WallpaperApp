package com.gunishjain.wallpaperapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.gunishjain.wallpaperapp.databinding.ActivityMainBinding
import com.gunishjain.wallpaperapp.ui.fragments.WallpapersListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentContainer: FrameLayout
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragment = WallpapersListFragment()
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}