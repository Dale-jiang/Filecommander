package com.tqs.filecommander.ui.activity

import android.text.Html
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.adapter.DocAdapter
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityDocListBinding
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.vm.MainVM

class DocListActivity : BaseActivity<ActivityDocListBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_doc_list
    override val TAG: String
        get() = this.packageName
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            onBackPressed()
        }
        viewModel.deletedFile.observe(this){
            if (it)
                getDocDataList()
        }
        getDocDataList()
        viewModel.dataList.observe(this) {
            viewModel.statisticsListDirectory(it){folders->
                binding.tvFileDescription.text =
                    Html.fromHtml("<font color='#000000'><big><big>${folders}</big></big></font>  folders and <font color='#000000'><big><big>${it.size}</big></big></font> files")

            }
            if (it.size > 0) {
                viewModel.statisticsFileType(it)
            }
        }
        viewModel.listSelectCount.observe(this) {
            when (it > 0) {
                true -> {
                    binding.vFileDelete.setBackgroundResource(R.drawable.bg_delete_selected)
                }

                false -> {
                    binding.vFileDelete.setBackgroundResource(R.drawable.bg_delete_normal)
                }
            }
        }
        viewModel.mDocAdapter = DocAdapter(this, viewModel.docDataList)
        binding.lvFileList.adapter = viewModel.mDocAdapter
        binding.lvFileList.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            viewModel.mDocAdapter.setSelected(true)
            viewModel.docDataList[position].selected = true
            viewModel.mDocAdapter.setData(viewModel.docDataList)
            viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
            true
        }

        binding.lvFileList.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            if (viewModel.mDocAdapter.getSelected()) {
                if (viewModel.docDataList[position].selected) {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                } else {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                }
                viewModel.docDataList[position].selected = !viewModel.docDataList[position].selected
                viewModel.mDocAdapter.setData(viewModel.docDataList)
            }
        }
        binding.vFileDelete.setOnClickListener {
//            AdsManager.adsInsertResultClean.showFullScreenAds(this@DocListActivity) {
                viewModel.showDeleteDialog(this, cancel = {}, confirm = {
                    viewModel.deleteSelectedDoc{

                    }
                    getDocDataList()
                })
//            }
        }
    }

    private fun getDocDataList() {
        when (intent.getStringExtra(Common.PAGE_TYPE)) {
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

    override fun onBackPressed() {
        viewModel.onBackPressed(this@DocListActivity,{
            super.onBackPressed()
        },{
        })

    }
}