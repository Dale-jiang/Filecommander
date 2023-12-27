package com.tqs.filecommander.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.tqs.filecommander.model.FileEntity
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat


object FileUtils {
    private var mContentResolver: ContentResolver? = null
    private var mCursor: Cursor? = null

    val TYPEIMAGE = 1
    val TYPEVIDEO = 2
    val TYPEAUDIO = 3
    val TYPEDOCUMENT = 4
    val TYPEDOWNLOAD = 5
    fun getTwoDigits(number: Int): Float {
        return getTwoDigits(number.toLong())
    }

    fun getTwoDigits(number: Float): Float {
        return getTwoDigits(number.toLong())
    }

    fun getTwoDigits(number: Long): Float {
        val format = DecimalFormat("0.##")
        format.roundingMode = RoundingMode.FLOOR
        return format.format(number).toFloat()
    }

    fun getTwoDigitsSpace(number: Int): String {
        return getTwoDigitsSpace(number.toLong())
    }

    fun getTwoDigitsSpace(number: Long): String {
        val format = DecimalFormat("0.##")
        format.roundingMode = RoundingMode.FLOOR
        return when (number) {
            in 0..0 -> {
                "0"
            }

            in 1..1023 -> {
                "${format.format(number)}B"
            }

            in 1024..<1024 * 1024 -> {
                "${format.format(number / 1024.0)}KB"
            }

            in 1024 * 1024..<1024 * 1024 * 1024 -> {
                "${format.format(number / 1024.0 / 1024.0)}MB"
            }

            else -> {
                "${format.format(number / 1024.0 / 1024.0 / 1024.0)}GB"
            }
        }
    }

    fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    private fun getContentProvider(context: Context) {
        if (mContentResolver == null) {
            mContentResolver = context.contentResolver
        }
    }

    fun getImgListOrderDescByDate(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.jpg'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.jpeg'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.png'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.gif'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.bmp'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.webp'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.heif'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.raw'"
        return getFileList(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            selection,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc",
            TYPEIMAGE
        )
    }

    fun getImgListOrderAscByDate(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.jpg'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.jpeg'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.png'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.gif'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.bmp'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.webp'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.heif'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.raw'"
        return getFileList(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            selection,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED + "  asc",
            TYPEIMAGE
        )
    }

    fun getVideoListOrderDescByDate(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.mkv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.avi'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.flv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.wmv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.rmvb'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mp4'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mov'"
        return getFileList(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            selection,
            null,
            MediaStore.Video.Media.DATE_MODIFIED + " desc",
            TYPEVIDEO
        )
    }

    fun getVideoListOrderAscByDate(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.mkv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.avi'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.flv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.wmv'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.rmvb'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mp4'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mov'"
        return getFileList(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            selection,
            null,
            MediaStore.Video.Media.DATE_MODIFIED + " asc",
            TYPEVIDEO
        )
    }

    fun getAudioList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.3gp'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mp3'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.ogg'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.flac'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.mid'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.wav'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.m4a'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.aac'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.wma'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.aiff'"
        return getFileList(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            selection,
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER,
            TYPEAUDIO
        )
    }


    fun getDocList(context: Context): ArrayList<FileEntity> {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.docx'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.xlsx'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.pptx'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.pdf'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.txt'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.rtf'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.html'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.xml'" +
                "or ${MediaStore.Files.FileColumns.DATA} LIKE '%.csv'"

        return getFileList(
            MediaStore.Files.getContentUri("external"),
            null,
            selection,
            null,
            MediaStore.Files.FileColumns.DATE_MODIFIED + "  desc",
            TYPEDOCUMENT
        )

    }

    fun getDownloadList(context: Context): ArrayList<FileEntity>? {
        getContentProvider(context)
        val selection = "${MediaStore.Files.FileColumns.DATA} LIKE '%.apk'"
        return getFileList(
            MediaStore.Files.getContentUri("external"),
            null,
            selection,
            null,
            MediaStore.Files.FileColumns.DATE_MODIFIED + "  desc",
            TYPEDOWNLOAD
        )
    }


    @SuppressLint("Range")
    private fun getFileList(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?,
        fileType: Int
    ): ArrayList<FileEntity> {
        mCursor = mContentResolver?.query(uri, projection, selection, selectionArgs, sortOrder)
        val fileEntities: MutableList<FileEntity> = ArrayList<FileEntity>()
        try {
            val mFiles: MutableList<String> = ArrayList()
            while (mCursor!!.moveToNext()) {
                val id = mCursor!!.getInt(mCursor!!.getColumnIndex(MediaStore.Audio.Media._ID))
                val path =
                    mCursor!!.getString(mCursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                val size = mCursor!!.getInt(mCursor!!.getColumnIndex(MediaStore.Audio.Media.SIZE))
                val mimetype =
                    mCursor!!.getString(mCursor!!.getColumnIndex(MediaStore.Audio.Media.MIME_TYPE))
                val dateAdd =
                    mCursor!!.getLong(mCursor!!.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                if (!mFiles.contains(path)) {
                    mFiles.add(path)
                    val fileEntity = FileEntity()
                    fileEntity.id = id
                    fileEntity.path = path
                    fileEntity.size = size
                    if (path.isNotEmpty()) {
                        val olaName = path.substring(path.lastIndexOf("/") + 1, path.length)
                        fileEntity.name = olaName
                    }
                    fileEntity.mimeType = mimetype
                    fileEntity.date = dateAdd
                    fileEntity.fileType = fileType
                    fileEntities.add(fileEntity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mCursor?.close()
        }
        return fileEntities as ArrayList<FileEntity>
    }

}