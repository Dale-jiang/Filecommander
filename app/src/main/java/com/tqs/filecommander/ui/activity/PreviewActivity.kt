package com.tqs.filecommander.ui.activity

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tqs.filecommander.R
import com.tqs.filecommander.adapter.PreviewAdapter
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityImagePreviewBinding
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class PreviewActivity : BaseActivity<ActivityImagePreviewBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_preview
    override val TAG: String
        get() = this.packageName

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        viewModel.currentIndex = intent.getIntExtra("selectImageIndex", 0)
        object : TypeToken<ArrayList<String>>() {}.type
        viewModel.previewMediaList = Gson().fromJson(
            intent.getStringExtra("previewFileList"),
            object : TypeToken<ArrayList<FileEntity>>() {}.type
        ) as MutableList<FileEntity>
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        setTitleText()
        viewModel.mPreviewAdapter = PreviewAdapter(this, viewModel.previewMediaList) {position ->
            if (binding.clMenu.isVisible)
                binding.clMenu.visibility = View.GONE
            else
                binding.clMenu.visibility = View.VISIBLE
        }
        binding.vpShowMedia.adapter = viewModel.mPreviewAdapter
        binding.vpShowMedia.offscreenPageLimit = 0
        binding.vpShowMedia.currentItem = viewModel.currentIndex
        binding.vpShowMedia.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                viewModel.currentIndex = position
                if (Common.VIDEO_LIST == viewModel.mPageType) {
                    viewModel.stopPlayer()
                }
                setTitleText()
                binding.clMenu.visibility = View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
        binding.ivWarningImage.setOnClickListener {
//            AdsManager.adsInsertResultClean.showFullScreenAds(this@PreviewActivity) {
                viewModel.setPopupWindow(this@PreviewActivity,binding.titleBar)
//            }
        }
        binding.ivDeleteImage.setOnClickListener {
//            AdsManager.adsInsertResultClean.showFullScreenAds(this@PreviewActivity){
                viewModel.showDeleteDialog(this@PreviewActivity, cancel = {}) {
                    viewModel.deleteSelectPreview(binding.vpShowMedia) {
                        setTitleText()
                        if (viewModel.previewMediaList!!.isEmpty()){
                            finish()
                        }
                    }
                }
//            }
        }
        binding.ivShareImage.setOnClickListener {
//            AdsManager.adsInsertResultScan.showFullScreenAds(this@PreviewActivity) {
                viewModel.setSharedFile(this@PreviewActivity)
//            }
        }
    }

    private fun setTitleText() {
        binding.titleBar.setTitleText("${viewModel.currentIndex + 1} / ${viewModel.previewMediaList!!.size}")
    }

    override fun onBackPressed() {
        viewModel.onBackPressed(this@PreviewActivity,{
            super.onBackPressed()
        },{

        })

    }


}