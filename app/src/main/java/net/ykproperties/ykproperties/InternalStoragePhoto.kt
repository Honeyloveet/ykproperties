package net.ykproperties.ykproperties

import android.graphics.Bitmap
import android.net.Uri

data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap,
    val uri: Uri,
    val absolutePath: String
)