package com.gunishjain.wallpaperapp.ui.activities

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.databinding.ActivityMainBinding
import com.gunishjain.wallpaperapp.ui.fragments.CategoryListFragment
import com.gunishjain.wallpaperapp.ui.fragments.FavouriteWallpaperFragment
import com.gunishjain.wallpaperapp.ui.fragments.SearchWallpaperFragment
import com.gunishjain.wallpaperapp.ui.fragments.WallpapersListFragment
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.Query

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val fragment = WallpapersListFragment()
    private val categoryFragment = CategoryListFragment()
    private val favouriteFragment = FavouriteWallpaperFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager= AppUpdateManagerFactory.create(applicationContext)
        if(updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdates()
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
            when (it.itemId) {
                R.id.favourite -> {
                    gotoFavouriteFragment()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                }
                R.id.share -> {
                    shareApp()
                    binding.mainDrawer.closeDrawer(GravityCompat.START)
                }
                R.id.rating -> {
                    rateApp()
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

        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (menu != null) {
            (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))


                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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


    private fun setUpViews() {
        setUpDrawerLayout()
    }

    private fun setUpDrawerLayout() {
        setSupportActionBar(binding.appBar)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.mainDrawer, R.string.open, R.string.close)
        binding.mainDrawer.addDrawerListener(actionBarDrawerToggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBarDrawerToggle.syncState()

    }

    private fun gotoFavouriteFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentContainer.id, favouriteFragment)
        fragmentTransaction.addToBackStack("favourite_fragment")
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
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

    private fun rateApp() {
        val appPackageName = packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
            startActivity(webIntent)
        }
    }

    // Function to share the app
    private fun shareApp() {

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val shareBody = "Check out this amazing app: https://play.google.com/store/apps/details?id=com.gunishjain.wallpaperapp"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

    }

    override fun onResume() {
        super.onResume()
        if(updateType==AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        123
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123){
            if(resultCode!= RESULT_OK){
                println("Something went wrong updating!")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(updateType==AppUpdateType.FLEXIBLE){
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
    }

    private val installStateUpdatedListener= InstallStateUpdatedListener {state->
        if(state.installStatus()==InstallStatus.DOWNLOADED){
            Toast.makeText(
                applicationContext,
                "Download successful. Restarting app in 5 sec",
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                delay(5000)
                appUpdateManager.completeUpdate()
            }
        }

    }

    private fun checkForAppUpdates(){
        appUpdateManager.appUpdateInfo.addOnSuccessListener {info->
        val isUpdateAvailable= info.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed= when(updateType){
                AppUpdateType.FLEXIBLE->info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE->info.isImmediateUpdateAllowed
                else -> false
            }

            if(isUpdateAvailable && isUpdateAllowed){
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    123
                )
            }
        }
    }
}