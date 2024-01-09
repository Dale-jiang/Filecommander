package com.tqs.filecommander.vm

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.StatFs
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.adapter.DocAdapter
import com.tqs.filecommander.adapter.ImageVideoListAdapter
import com.tqs.filecommander.adapter.PreviewAdapter
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.base.BaseVM
import com.tqs.filecommander.model.DocumentEntity
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.ui.activity.DocListActivity
import com.tqs.filecommander.ui.activity.ImageListActivity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.ui.view.FileDetailPopupWindow
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.utils.FileUtils
import java.io.File

class MainVM : BaseVM() {

    var mainOnBackPressedTime: Long = 0L
    override lateinit var title: LiveData<String>

    val DESC: String = "DESC"
    val ASC: String = "ASC"
    val ALL: String = "ALL"
    val NONE: String = "NONE"

    var currentIndex = 0
    var baseAds: BaseAds? = null
    val countDownTime = 10 * 1000L
    var deletedFile = MutableLiveData<Boolean>(false)
    var mPageType: String = Common.IMAGE_LIST

    lateinit var mDocAdapter: DocAdapter
    lateinit var mPreviewAdapter: PreviewAdapter
    lateinit var mImageAdapter: ImageVideoListAdapter

    var dataList = MutableLiveData<ArrayList<FileEntity>>()
    var listSelectCount = MutableLiveData<Int>(0)

    var previewMediaList: MutableList<FileEntity>? = null
    var docDataList: ArrayList<DocumentEntity> = arrayListOf()
    var showDataList: ArrayList<FileEntity> = arrayListOf()

    var mDeleteDialog: ConfirmAndCancelDialog? = null
    var mPopupWindow: FileDetailPopupWindow? = null

    var currentOrder = MutableLiveData<String>(DESC)
    var currentSelect = MutableLiveData<String>(NONE)

    var totalSpace = MutableLiveData<String>()
    var availSpace = MutableLiveData<String>()
    var progressValue = MutableLiveData<Int>(0)

    var imageList = MutableLiveData<ArrayList<FileEntity>>()
    var audioList = MutableLiveData<ArrayList<FileEntity>>()
    var videoList = MutableLiveData<ArrayList<FileEntity>>()
    var documentsList = MutableLiveData<ArrayList<FileEntity>>()
    var downloadList = MutableLiveData<ArrayList<FileEntity>>()

    var imageSpaceSize = MutableLiveData<String>()
    var audioSpaceSize = MutableLiveData<String>()
    var videoSpaceSize = MutableLiveData<String>()
    var documentsSpaceSize = MutableLiveData<String>()
    var downloadSpaceSize = MutableLiveData<String>()

    fun getMemoryInfo() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sf = StatFs(Environment.getExternalStorageDirectory().path)
            val blockSize = sf.blockSizeLong
            val blockCount = sf.blockCountLong
            val availCount = sf.availableBlocksLong
            totalSpace.value = FileUtils.getTwoDigitsSpace(blockSize * blockCount)
            availSpace.value = FileUtils.getTwoDigitsSpace((blockCount - availCount) * blockSize)
            progressValue.value = ((blockCount - availCount) * 1.0 / blockCount * 100).toInt()
        }
    }

    fun getImageListSize() {
        var size = 0L
        for (fileEntity in imageList.value!!) {
            size += fileEntity.size
        }
        imageSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getVideoListSize() {
        var size = 0L
        for (fileEntity in videoList.value!!) {
            size += fileEntity.size
        }
        videoSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getAudioListSize() {
        var size = 0L
        for (fileEntity in audioList.value!!) {
            size += fileEntity.size
        }
        audioSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getDocumentsListSize() {
        var size = 0L
        for (fileEntity in documentsList.value!!) {
            size += fileEntity.size
        }
        documentsSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getDownloadListSize() {
        var size = 0L
        for (fileEntity in downloadList.value!!) {
            size += fileEntity.size
        }
        downloadSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getImageList(context: Context) {
        getImageListOrderDescByDate(context)
        imageList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    fun getVideoList(context: Context) {
        getVideoListOrderDescByDate(context)
        videoList.value = FileUtils.getVideoListOrderDescByDate(context)
    }

    fun getAudioList(context: Context) {
        dataList.value = FileUtils.getAudioList(context)
        audioList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context) {
        dataList.value = FileUtils.getDocList(context)
        documentsList.value = FileUtils.getDocList(context)
    }

    fun getDownloadList(context: Context) {
        dataList.value = FileUtils.getDownloadList(context)
        downloadList.value = FileUtils.getDownloadList(context)
    }

    private fun getVideoListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderDescByDate(context)
    }

    private fun getVideoListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderAscByDate(context)
    }

    private fun getImageListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    private fun getImageListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderAscByDate(context)
    }

    private fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
        val showDataList: MutableList<FileEntity> = mutableListOf()
        var date = ""
        if (list != null) {
            for (fileEntity in list) {
                val yyMM = DateUtils.getMonthTimeBySecond(fileEntity.date)
                if (yyMM != date) {
                    showDataList.add(FileEntity(isTitle = true, date = fileEntity.date))
                    date = yyMM
                }
                showDataList.add(fileEntity)
            }
        }
        return showDataList as ArrayList<FileEntity>
    }

    fun getEmptyData(position: Int, showDataList: ArrayList<FileEntity>?): Int {
        var count: Int = 0
        for (i in 0..showDataList?.size!!) {
            if (i <= position && showDataList[i].isTitle) {
                count++
            }
        }
        return count
    }

    @SuppressLint("NotifyDataSetChanged")
    fun unselectAllShowImageList() {
        listSelectCount.value = 0
        for (file in showDataList) {
            file.selected = false
        }
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getImageViewShowList() {
        showDataList = changeShowImageList(dataList.value)
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun selectAllShowImageList() {
        listSelectCount.value = 0
        for (file in showDataList) {
            if (!file.isTitle) {
                file.selected = true
                listSelectCount.value = listSelectCount.value!! + 1
            }
        }
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    fun orderShowImageList(context: Context, title: (String) -> Unit) {
        dataList.value?.clear()
        when (currentOrder.value) {
            DESC -> {
                when (mPageType) {
                    Common.IMAGE_LIST -> {
                        getImageListOrderDescByDate(context)
                        title.invoke("Image")
                    }

                    Common.VIDEO_LIST -> {
                        getVideoListOrderDescByDate(context)
                        title.invoke("Video")
                    }
                }
            }

            ASC -> {
                when (mPageType) {
                    Common.IMAGE_LIST -> {
                        getImageListOrderAscByDate(context)
                        title.invoke("Image")
                    }

                    Common.VIDEO_LIST -> {
                        getVideoListOrderAscByDate(context)
                        title.invoke("Video")
                    }
                }
            }
        }
    }

    fun showDeleteDialog(context: Context, title: String = "", content: String = "", cancel: () -> Unit, confirm: () -> Unit) {
        if (mDeleteDialog == null) {
            mDeleteDialog = ConfirmAndCancelDialog(context, {
                mDeleteDialog?.dismiss()
                cancel.invoke()
            }, {
                mDeleteDialog?.dismiss()
                confirm.invoke()
            })

        }
        mDeleteDialog?.setTitle(title)
        mDeleteDialog?.setContent(content)
        mDeleteDialog?.show()
    }

    fun deleteSelectPreview(viewGroup: ViewGroup, setTitleText: () -> Unit) {
        if (FileUtils.deleteFile(previewMediaList!![currentIndex].path!!)) {
            deletedFile.value = true
            previewMediaList!!.remove(previewMediaList?.get(currentIndex))
            mPreviewAdapter.setData(previewMediaList!!)
            mPreviewAdapter.destroyItem(viewGroup, -1, viewGroup.rootView)
            mPreviewAdapter.notifyDataSetChanged()
            setTitleText()
        }
    }

    fun deleteSelectedDoc(function: (Int) -> Unit) {
        for (doc in docDataList) {
            if (doc.selected) {
                for (file in doc.docPathList) {
                    FileUtils.deleteFile(file.path!!)
                }
            }
        }
        docDataList.removeIf {
            it.selected
        }
        mDocAdapter.setData(docDataList)
        mDocAdapter.setSelected(false)
        listSelectCount.value = 0
        deletedFile.value = true
        function.invoke(AppCompatActivity.RESULT_OK)
    }

    fun deleteSelectedImage(function: () -> Unit) {
        for (file in showDataList) {
            if (file.selected) {
                file.path?.let { FileUtils.deleteFile(it) }
            }
        }
        mImageAdapter.touchState = mImageAdapter.CLICKSTATE
        listSelectCount.value = 0
        function.invoke()
    }

    fun onImageAdapterItemClick(touchView: String, position: Int, title: (String) -> Unit, preview: (Int) -> Unit) {
        if (mImageAdapter.touchState == mImageAdapter.LONGSTATE
            && touchView != mImageAdapter.TOUCHTITLEVIEW
        ) {
            if (showDataList[position].selected) {
                listSelectCount.value = listSelectCount.value?.minus(1)
                currentSelect.value = NONE
                title.invoke("select all")
            } else {
                listSelectCount.value = listSelectCount.value?.plus(1)
                if (listSelectCount.value == dataList.value?.size) {
                    currentSelect.value = ALL
                    title.invoke("unselect")
                }
            }
            showDataList[position].selected = !showDataList[position].selected
            mImageAdapter.setData(showDataList)
            mImageAdapter.notifyItemChanged(position)
        } else {
            when (touchView) {
                mImageAdapter.TOUCHIMAGEVIEW -> {
                    preview.invoke(position)
                }

                mImageAdapter.TOUCHPLAYVIEW -> {
                    preview.invoke(position)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onImageAdapterItemLongClick(position: Int, viewFun: () -> Unit) {
        if (!showDataList[position].selected && mImageAdapter.touchState != mImageAdapter.LONGSTATE) {
            mImageAdapter.touchState = mImageAdapter.LONGSTATE
            showDataList[position].selected = !showDataList[position].selected
            mImageAdapter.setData(showDataList)
            viewFun.invoke()
            listSelectCount.value = 1
            mImageAdapter.notifyDataSetChanged()
        }
    }

    fun setSelectText(function: (String) -> Unit) {
        when (currentSelect.value) {
            NONE -> {
                selectAllShowImageList()
                currentSelect.value = ALL
                function.invoke("unselect")
            }

            ALL -> {
                unselectAllShowImageList()
                currentSelect.value = NONE
                function.invoke("select all")
            }
        }
    }


    fun statisticsFileType(fileEntities: ArrayList<FileEntity>) {
        val hashMap = HashMap<String, DocumentEntity>()
        for (file in fileEntities) {
            file.path?.let {
                val suffix = it.substring(it.lastIndexOf(".", it.length))
                var document = hashMap[suffix]
                if (document == null) {
                    document = DocumentEntity(suffix = suffix, typeFile = file.fileType, number = 1, selected = false, docPathList = mutableListOf())
                } else {
                    document.number = document.number + 1
                }
                document.docPathList.add(file)
                hashMap.put(suffix, document)
            }
        }
        docDataList = ArrayList<DocumentEntity>(hashMap.values)
        mDocAdapter.setData(docDataList)
    }

    fun statisticsListDirectory(fileEntities: ArrayList<FileEntity>, function: (Int) -> Unit) {
        val hashSet = HashSet<String>()
        for (file in fileEntities) {
            file.path?.let {
                val dir = it.substring(0, it.lastIndexOf("/"))
                hashSet.add(dir)
            }
        }
        function.invoke(hashSet.size)
    }


    public fun setSharedFile(activity: Activity) {
        val uri = FileProvider.getUriForFile(activity, "${activity.packageName}.fileprovider", File(previewMediaList?.get(currentIndex)?.path ?: ""))
        val type = previewMediaList?.get(currentIndex)?.mimeType

        activity.startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            setType(type)
        }, "shared  file to:"))
    }

    public fun stopPlayer() {
        mPreviewAdapter.stopPlayer()
    }

    public fun setPopupWindow(context: Context, view: View) {
        if (mPopupWindow == null) {
            mPopupWindow = FileDetailPopupWindow(context)
            previewMediaList?.let { mPopupWindow?.setFileInfo(it[currentIndex]) }
        }
        mPopupWindow?.showAsDropDown(view, 0, 0, Gravity.CENTER)
    }

    fun onBackPressed(activity: Activity, function: () -> Unit, viewFun: () -> Unit) {
        deletedFile.value = false
        when (activity) {
            is ImageListActivity -> {
                if (listSelectCount.value!! > 0 || mImageAdapter.touchState == mImageAdapter.LONGSTATE) {
                    mImageAdapter.touchState = mImageAdapter.CLICKSTATE
                    unselectAllShowImageList()
                } else {
                    function.invoke()
                }
            }

            is DocListActivity -> {
                if (listSelectCount.value!! > 0 || mDocAdapter.getSelected()) {
                    for (doc in docDataList) {
                        doc.selected = false
                    }
                    mDocAdapter.setSelected(false)
                    mDocAdapter.setData(docDataList)
                    listSelectCount.value = 0
                } else {
                    function.invoke()
                }
            }
        }
    }

}