package com.tqs.filecommander.ui.activity

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tqs.filecommander.R
import com.tqs.filecommander.adapter.ImageVideoListAdapter
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityImageListBinding
import com.tqs.filecommander.ui.view.MediaItemDecoration
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class ImageListActivity : BaseActivity<ActivityImageListBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName

    private var registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {

        }
    }


    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            onBackPressed()
        }
        viewModel.deletedFile.observe(this){
            if (it)
                viewModel.getImageViewShowList()
        }
        binding.titleBar.setOrderVisible(true)
        binding.titleBar.setOrderClickListener {
            viewModel.currentOrder.value = when (viewModel.currentOrder.value) {
                viewModel.DESC -> viewModel.ASC
                viewModel.ASC -> viewModel.DESC
                else -> {
                    ""
                }
            }
            viewModel.orderShowImageList(this) {
                binding.titleBar.setTitleText(it)
            }
        }
        binding.vImageDelete.visibility = View.GONE
        binding.titleBar.setSelectClickListener {
            viewModel.setSelectText {
                binding.titleBar.setSelectText(it)
            }
        }
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        viewModel.orderShowImageList(this) {
            binding.titleBar.setTitleText(it)
        }
        viewModel.listSelectCount.observe(this) {
            if (it > 0) {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_selected)
            } else {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_normal)
            }
        }
        viewModel.dataList.observe(this) {
            viewModel.getImageViewShowList()
        }
        binding.vImageDelete.setOnClickListener {
//            AdsManager.adsInsertResultClean.showFullScreenAds(this@ImageListActivity) {
                viewModel.showDeleteDialog(this, cancel =  {

                }, confirm = {
                    viewModel.deleteSelectedImage {
                        binding.titleBar.setOrderVisible(true)
                        binding.vImageDelete.visibility = View.GONE

                    }
                    viewModel.orderShowImageList(this) {
                        binding.titleBar.setTitleText(it)
                    }

                })

//            }
        }
        setImageListAdapter()
    }

    private fun setImageListAdapter() {

        val manager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (viewModel.showDataList[position].isTitle) {
                    3
                } else {
                    1
                }
            }
        }
        binding.rvImageList.layoutManager = manager
        binding.rvImageList.addItemDecoration(MediaItemDecoration(6))
        viewModel.mImageAdapter = ImageVideoListAdapter(this, viewModel.showDataList, { position, touchView ->
            viewModel.onImageAdapterItemClick(touchView, position, {
                binding.titleBar.setSelectText(it)
            }, {
                toPreviewImage(it)
            })
        }, { position ->
            viewModel.onImageAdapterItemLongClick(position) {
                binding.vImageDelete.visibility = View.VISIBLE
                binding.titleBar.setSelectVisible(true)
            }

        })
        binding.rvImageList.adapter = viewModel.mImageAdapter

    }

    private fun toPreviewImage(position: Int) {
        registerForActivityResult.launch(Intent(this, PreviewActivity::class.java).apply {
            putExtra("selectImageIndex", position - viewModel.getEmptyData(position, viewModel.showDataList))
            putExtra("previewFileList", Gson().toJson(viewModel.dataList.value))
            putExtra(Common.PAGE_TYPE, viewModel.mPageType)
        })
    }

    override fun onBackPressed() {
        viewModel.onBackPressed(this@ImageListActivity, {
            super.onBackPressed()
        }, {
            binding.titleBar.setOrderVisible(true)
            binding.vImageDelete.visibility = View.GONE
        })

    }
}