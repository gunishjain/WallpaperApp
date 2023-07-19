package com.gunishjain.wallpaperapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.gunishjain.wallpaperapp.R
import com.gunishjain.wallpaperapp.databinding.FragmentSetWallpaperDialogueBinding
import com.gunishjain.wallpaperapp.data.models.Photo
import com.gunishjain.wallpaperapp.ui.viewmodels.SetWallpaperDialogueViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SetWallpaperDialogue(wallpaper: Photo) : DialogFragment() {

    private lateinit var binding: FragmentSetWallpaperDialogueBinding
    private val setWPDialogueVM: SetWallpaperDialogueViewModel by viewModels()
    private val wallpaper : Photo = wallpaper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetWallpaperDialogueBinding.inflate(inflater,container,false)

        binding.btnApply.setOnClickListener {

            val wpFlag: Int = when(binding.radioGroup.checkedRadioButtonId){
                R.id.setHome-> 1
                R.id.setLock-> 2
                R.id.setBoth-> 3
                else -> 0
            }

            Log.d("wpdialoguefragment",wpFlag.toString())
            setWPDialogueVM.setWallPaper(wpFlag,wallpaper)
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}