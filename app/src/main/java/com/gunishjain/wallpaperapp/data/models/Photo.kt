package com.gunishjain.wallpaperapp.data.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "wallpapers_table")
@Parcelize
data class Photo(
    val alt: String,
    val avg_color: String,
    val height: Int,
    @PrimaryKey
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    @Embedded
    val src: Src,
    val url: String,
    val width: Int
) : Parcelable