package com.tqs.filecommander.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * used for loading provider's data
 */
@Keep
@Parcelize
data class FileEntity(
    var id: Int = 0,
    var path: String? = "",
    var size: Int = 0,
    var name: String? = "",
    var mimeType: String? = "",
    var date: Long = 0L,
    var fileType: Int = 1,
    // true is recyclerView showed title
    var isTitle: Boolean = false,
    // true is user selected to delete,just selected or not delete
    var selected: Boolean = false,
    // true is user delete this data ,for no delete and need showing next launch app
    var deleted: Boolean = false
) : Parcelable
