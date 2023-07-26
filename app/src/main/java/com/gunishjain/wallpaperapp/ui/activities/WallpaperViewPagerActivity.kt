package com.gunishjain.wallpaperapp.ui.activities


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.ui.adapters.SingleWallpaperAdapter
import com.gunishjain.wallpaperapp.databinding.ActivityWallpaperViewPagerBinding
import com.gunishjain.wallpaperapp.paging.SingleWallpaperPagingAdapter
import com.gunishjain.wallpaperapp.ui.fragments.FullScreenFragment
import com.gunishjain.wallpaperapp.ui.fragments.SetWallpaperDialogue
import com.gunishjain.wallpaperapp.ui.viewmodels.WallPaperListViewModel
import com.gunishjain.wallpaperapp.ui.viewmodels.WallpaperViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WallpaperViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperViewPagerBinding
//    private lateinit var singleWallpaperAdapter: SingleWallpaperAdapter
    private lateinit var singleWallpaperAdapter :SingleWallpaperPagingAdapter
    private val wallpaperVPVM: WallpaperViewPagerViewModel by viewModels()
    private val wallpaperListVM: WallPaperListViewModel by viewModels()
    private var category : String? = null
    private var singlePhoto : Photo? = null
    private lateinit var photoListWithSinglePhoto: ArrayList<Photo>
    private var palette: Palette? = null
    private var savedWallpapers: List<Photo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        category = intent.getStringExtra("category")
        singlePhoto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("photo", Photo::class.java)
        } else {
            intent.getParcelableExtra("photo") as Photo?
        }

        photoListWithSinglePhoto = ArrayList()

//        category?.let { wallpaperListVM.getWallPaperList(it) }

        category?.let { wallpaperListVM.getWallpapersPaginated(it) }



        singleWallpaperAdapter = SingleWallpaperPagingAdapter()
//        singleWallpaperAdapter.setWallpaperViewPager(photoListWithSinglePhoto,binding.viewPager2)

        singleWallpaperAdapter.setWallpaperViewPager(binding.viewPager2)

        binding.viewPager2.adapter = singleWallpaperAdapter



        wallpaperVPVM.getSavedWallpapers()
        observeSavedWallpapers()

//        observeWallPaperList()
        observeWallPaperListPaginated()
        setupTransformer()

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

//                val currentPhoto = photoListWithSinglePhoto[position]
                val currentList = singleWallpaperAdapter.snapshot().items

                // Check if the position is valid within the current list
                if (position >= 0 && position < currentList.size) {
                    // Get the current photo from the list
                    val currentPhoto = currentList[position]


                    val imageUrl = currentPhoto.src.portrait


                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(imageUrl)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(
                                bitmap: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                palette = Palette.from(bitmap).generate()
                            }

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                Log.d("ViewPagerActivity", errorDrawable.toString())
                            }
                        })

                    val startColor = palette?.dominantSwatch?.rgb ?: 0
                    val endColor = palette?.lightVibrantSwatch?.rgb ?: 0

                    binding.viewPager2.background = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(startColor, endColor)
                    )
                }
            }
        })

        onSetWallpaperClick()
        onWallpaperSave()
        onWallpaperDownload()
        onWallpaperClick()

    }

    private fun onWallpaperClick() {
        singleWallpaperAdapter.setOnItemClickListener { photo->
            val fragment = FullScreenFragment()

            fragment.getPhotoData(photo)
            supportFragmentManager.beginTransaction()
                .replace(binding.fullscreenContainer.id, fragment)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun onWallpaperDownload() {
        singleWallpaperAdapter.setOnDownloadClickListener {wallpaper->
            wallpaperVPVM.downloadWallpaper(wallpaper)
            Toast.makeText(applicationContext,"Wallpaper Downloaded",Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeSavedWallpapers() {

        wallpaperVPVM.observeSavedWallpapers().observe(this) {savedList->

            if(savedList.isNullOrEmpty()){
                Toast.makeText(this,"Empty List",Toast.LENGTH_SHORT).show()
            } else {
                savedWallpapers = savedList
            }
        }

    }

    private fun onSetWallpaperClick() {
        singleWallpaperAdapter.setOnWPItemClickListener {photo->
            SetWallpaperDialogue(photo).show(supportFragmentManager,"SetWallpaperDialogFragment")
        }
    }

    private fun onWallpaperSave(){
        singleWallpaperAdapter.setOnFavItemClickListener {photo->

            if(!photo.liked){
                photo.liked=true
                wallpaperVPVM.insertWallpaper(photo)
                Toast.makeText(this,"Wallpaper Saved",Toast.LENGTH_SHORT).show()
            } else {
                photo.liked=false
                wallpaperVPVM.deleteWallpaper(photo)
            }

            singleWallpaperAdapter.notifyDataSetChanged()
        }
    }

    private fun setupTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r=1- kotlin.math.abs(position)
            page.scaleY=0.85f + r * 0.14f
        }

        binding.viewPager2.setPageTransformer(transformer)
    }

    private fun observeWallPaperList() {
        wallpaperListVM.observeWallpaperListLiveData().observe(this) { photoList ->
            // Updates liked field of wallpapers from API based on Saved Wallpapers
            for (photo in photoList) {
                // Find the corresponding saved wallpaper photo.
                val savedWallpaperPhoto = savedWallpapers.find { it.id == photo.id }
                // If the saved wallpaper photo is found, update the photo list object's liked property.
                if (savedWallpaperPhoto != null) {
                    photo.liked = savedWallpaperPhoto.liked
                }
            }
            val index = photoList.indexOfFirst { it.id == singlePhoto!!.id }
            val photoArray = photoList.toMutableList()
            val photoClicked = photoList[index]
            photoArray.removeAt(index)
            photoArray.add(0,photoClicked)

            photoListWithSinglePhoto.addAll(photoArray)
//            singleWallpaperAdapter.setWallpaperViewPager(photoListWithSinglePhoto,binding.viewPager2)
            binding.viewPager2.apply {
                offscreenPageLimit=3
                clipToPadding=false
                clipChildren=false
                getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
            }
            singleWallpaperAdapter.notifyDataSetChanged()
        }
    }

    private fun observeWallPaperListPaginated() {


        val firstPhotoDataList = mutableListOf<Photo>()
            firstPhotoDataList.add(singlePhoto!!)

        val singlePagingData = PagingData.from(firstPhotoDataList)
        lifecycleScope.launch {

            singleWallpaperAdapter.submitData(singlePagingData)

            wallpaperListVM.paginatedWallpapers.collectLatest { pagingData ->

                val updatedPagingData =pagingData.map {photo->
                    val savedWallpaperPhoto = savedWallpapers.find { it.id == photo.id }
                    if (savedWallpaperPhoto != null) {
                        photo.liked = savedWallpaperPhoto.liked
                    }
                    photo
                }
                singleWallpaperAdapter.submitData(updatedPagingData)

                singleWallpaperAdapter.setWallpaperViewPager(binding.viewPager2)

                binding.viewPager2.apply {
                    offscreenPageLimit=3
                    clipToPadding=false
                    clipChildren=false
                    getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
                }
                singleWallpaperAdapter.notifyDataSetChanged()
            }
        }

    }


}