package com.gunishjain.wallpaperapp.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.databinding.FragmentFullScreenBinding
import com.gunishjain.wallpaperapp.ui.viewmodels.WallpaperViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FullScreenFragment : Fragment() {

    private var photo: Photo? = null
    private lateinit var binding: FragmentFullScreenBinding
    private val wallpaperVPVM: WallpaperViewPagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFullScreenBinding.inflate(inflater,container,false)

        val colorResId = if (photo!!.liked) R.color.red else R.color.white
        binding.imgFav.setColorFilter(ContextCompat.getColor(context!!, colorResId))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window
        val mainContainer = view.findViewById<ConstraintLayout>(R.id.constraint_layout)

        hideSystemUI(window,mainContainer)

        setWallpaper()
        onWallpaperDownload()
        onSetWallpaperClick()
        onWallpaperSave()
        onBackBtnClick()
    }

    private fun onBackBtnClick() {
        binding.btnBack.setOnClickListener {
            closeCurrentFragment()
        }
    }

    private fun setWallpaper() {
        Glide.with(requireContext())
            .load(photo?.src?.portrait)
            .into(binding.imgWallpaper)
    }

    fun getPhotoData(wallpaper: Photo){
        photo=wallpaper
    }

    private fun onWallpaperSave(){

        binding.imgFav.setOnClickListener {

            if(!photo?.liked!!){
                photo?.liked=true
                wallpaperVPVM.insertWallpaper(photo!!)
                Toast.makeText(context,"Wallpaper Saved", Toast.LENGTH_SHORT).show()
            } else {
                photo?.liked=false
                val colorResId = if (photo!!.liked) R.color.red else R.color.white
                binding.imgFav.setColorFilter(ContextCompat.getColor(context!!, colorResId))
                wallpaperVPVM.deleteWallpaper(photo!!)
                Toast.makeText(context,"Wallpaper Deleted From Favourites", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun onSetWallpaperClick() {
        binding.cvSetWallpaper.setOnClickListener {
            SetWallpaperDialogue(photo!!).show(childFragmentManager,"SetWallpaperDialogFragment")
        }
    }

    private fun onWallpaperDownload() {
        binding.imgDownload.setOnClickListener {
            wallpaperVPVM.downloadWallpaper(photo!!)
            Toast.makeText(context,"Wallpaper Downloaded",Toast.LENGTH_SHORT).show()
        }
    }

    private fun closeCurrentFragment() {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Check if the current fragment is added to the back stack
        val backStackEntryCount = fragmentManager.backStackEntryCount
        if (backStackEntryCount > 0) {
            // Remove the current fragment from the back stack
            fragmentManager.popBackStack()
        } else {
            // The current fragment is not added to the back stack, so just remove it
            fragmentManager.findFragmentById(R.id.img_SingleWallpaper)
                ?.let { fragmentTransaction.remove(it) }
        }

        fragmentTransaction.commit()
        showSystemUI(requireActivity().window,binding.root)
    }

    private fun hideSystemUI(window: Window,mainContainer: ConstraintLayout) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI(window: Window,mainContainer: ConstraintLayout) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars())
    }

}