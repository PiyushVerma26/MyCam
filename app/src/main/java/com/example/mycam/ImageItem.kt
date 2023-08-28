package com.example.mycam

import android.graphics.Bitmap

data class ImageItem(
    val bitmap: Bitmap,
    val timestamp: Long,
    var isFavorite: Boolean
)
