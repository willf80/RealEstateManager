package com.openclassrooms.realestatemanager.models

import android.graphics.Bitmap

class MediaTemp {
    var id: Long = 0
    var photo: Bitmap? = null
    var label: String? = null
    var fileName: String? = null
    var isCover: Boolean = false
}
