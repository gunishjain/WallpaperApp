package com.gunishjain.wallpaperapp.ui.activities


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gunishjain.wallpaperapp.Photo
import com.gunishjain.wallpaperapp.adapters.SingleWallpaperAdapter
import com.gunishjain.wallpaperapp.databinding.ActivityWallpaperViewPagerBinding
import com.gunishjain.wallpaperapp.viewmodels.WallPaperListViewModel


class WallpaperViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperViewPagerBinding
    private lateinit var singleWallpaperAdapter: SingleWallpaperAdapter
    private val wallpaperListVM: WallPaperListViewModel by viewModels()
    private var category : String? = null
    private var singlePhoto : Photo? = null
    private lateinit var photoListWithSinglePhoto: ArrayList<Photo>


    private var palette: Palette? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        category = intent.getStringExtra("category")
        singlePhoto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra<Photo>("photo", Photo::class.java)
        } else {
            intent.getParcelableExtra("photo") as Photo?
        }

        photoListWithSinglePhoto = ArrayList()
        photoListWithSinglePhoto.add(singlePhoto!!)

        category?.let { wallpaperListVM.getWallPaperList(it) }

        singleWallpaperAdapter = SingleWallpaperAdapter(photoListWithSinglePhoto,binding.viewPager2)
        binding.viewPager2.adapter = singleWallpaperAdapter

        observeWallPaperList()

        observeWallPaperList()
        setupTransformer()

        binding.viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                val currentPhoto = photoListWithSinglePhoto[position]

                val imageUrl = currentPhoto.src.large  // Choose the appropriate URL based on your requirements


                Glide.with(applicationContext)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                            // Use the bitmap for palette generation
                            palette = Palette.from(bitmap).generate()
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            Log.d("viewpageractivity",errorDrawable.toString())
                        }
                    })

                val startColor = palette?.dominantSwatch?.rgb?:0
                val endColor = palette?.lightVibrantSwatch?.rgb?:0

                binding.viewPager2.background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(startColor, endColor))
            }
        })
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

            photoListWithSinglePhoto.addAll(photoList)
            singleWallpaperAdapter = SingleWallpaperAdapter(photoListWithSinglePhoto,binding.viewPager2)
            binding.viewPager2.apply {
                offscreenPageLimit=3
                clipToPadding=false
                clipChildren=false
                getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER
            }
            singleWallpaperAdapter.notifyDataSetChanged()
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

    }
}