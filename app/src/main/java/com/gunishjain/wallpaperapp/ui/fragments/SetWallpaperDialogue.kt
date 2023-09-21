package com.gunishjain.wallpaperapp.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding= FragmentSetWallpaperDialogueBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireActivity())
        isCancelable = false
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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

        return dialog
    }

}