package com.tqs.filecommander.ui.activity

import android.content.Intent
import android.text.Html
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.adapter.DocAdapter
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityScannerResultBinding
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class ScannerResultActivity : BaseActivity<ActivityScannerResultBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_scanner_result
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        val notifyType = intent.getStringExtra(NotificationKey.notifyType)
        if (!notifyType.isNullOrBlank())
            TBAHelper.updatePoints(
                EventPoints.filecpop_all_result, mutableMapOf(
                    EventPoints.source to when (notifyType) {
                        NotificationKey.SCHEDULED -> "t"
                        NotificationKey.CHARGE -> "char"
                        NotificationKey.UNCLOCK -> "unl"
                        NotificationKey.UNINSTALL -> "uni"
                        else -> {
                            ""
                        }
                    }
                )
            )

        TBAHelper.updatePoints(EventPoints.filec_scan_result_show)
        binding.titleBar.setLeftClickListener {
            finish()
        }

        viewModel.dataList.observe(this) {
            viewModel.statisticsListDirectory(it) { folders ->
                binding.tvFileDescription.text =
                    Html.fromHtml("<font color='#000000'><big><big>${folders}</big></big></font>  folders and <font color='#000000'><big><big>${it.size}</big></big></font> files")

            }
            if (it.size > 0) {
                viewModel.statisticsFileType(it)
            }
        }
        getDocDataList()
        viewModel.mDocAdapter = DocAdapter(this, viewModel.docDataList)
        binding.vFileOk.setOnClickListener {
            TBAHelper.updatePoints(EventPoints.filec_scan_result_ok)
            jumpMediaListActivity(viewModel.mPageType)
        }
    }

    override fun onResume() {
        super.onResume()
        AdsManager.adsNativeResultScan.withLoad(this) {
            if (AdsManager.adsNativeResultScan.isCacheNotEmpty) {
                viewModel.baseAds?.destroyNative()
                kotlin.runCatching {
                    AdsManager.adsNativeResultScan.showNativeAds(this@ScannerResultActivity, binding.nativeFrame) {
                        viewModel.baseAds = it
                        Log.e(TAG, "native is show")
                    }
                }
            }
        }
    }

    private fun getDocDataList() {
        when (intent.getStringExtra(Common.PAGE_TYPE)) {
            Common.IMAGE_LIST -> {
                viewModel.getImageList(this)
                binding.titleBar.setTitleText("Images")
                binding.tvFileTitle.text = "Images"
            }

            Common.VIDEO_LIST -> {
                viewModel.getVideoList(this)
                binding.titleBar.setTitleText("Video")
                binding.tvFileTitle.text = "Video"
            }

            Common.AUDIO_LIST -> {
                viewModel.getAudioList(this)
                binding.titleBar.setTitleText("Audio")
                binding.tvFileTitle.text = "Audio"
            }

            Common.DOCUMENTS_LIST -> {
                viewModel.getDocumentsList(this)
                binding.titleBar.setTitleText("Documents")
                binding.tvFileTitle.text = "Documents"
            }

            Common.DOWNLOAD_LIST -> {
                viewModel.getDownloadList(this)
                binding.titleBar.setTitleText("Download")
                binding.tvFileTitle.text = "Download"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.baseAds?.destroyNative()
    }

}