package com.tqs.filecommander.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * used for showing audio、download file and documents
 */
@Keep
@Parcelize
data class DocumentEntity(
    var suffix: String = "",
    var number: Int = 0,
    var selected: Boolean = false,
    var typeFile: Int = -1,
    var docPathList: MutableList<FileEntity> = mutableListOf()
) : Parcelable
