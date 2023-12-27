package com.tqs.filecommander.ui.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.base.BaseFragment
import com.tqs.filecommander.databinding.FragmentFileCommanderBinding
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.vm.MainVM

class FileCommanderFragment : BaseFragment<FragmentFileCommanderBinding, MainVM>(),
    View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.fragment_file_commander


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFileCommanderBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        setClickListener()
        viewModel.deletedFile.observe(requireActivity()){
            if (it)
                setData()
        }
    }

    override fun onResume() {
        super.onResume()
        if((requireActivity() as BaseActivity<*, *>).checkPermissionExternal()){
            setData()
        }
    }

    private fun setData() {
        setProgressValue()
        setMediaList()
        setMediaListSize()
        getMediaInfo()
    }

    private fun setProgressValue() {
        viewModel.progressValue.observe(requireActivity()) {
            binding.mainProgressBar.progress = it
            binding.tvSpaceProportion.text =
                Html.fromHtml("<font color='#FF4F15'>${viewModel.availSpace.value}</font> / ${viewModel.totalSpace.value}")
        }
    }

    private fun setMediaList() {
        viewModel.imageList.observe(requireActivity()) {
            viewModel.getImageListSize()
            binding.tvImageTime.text = DateUtils.getCurrentTime()
        }
        viewModel.audioList.observe(requireActivity()) {
            viewModel.getAudioListSize()
            binding.tvAudioTime.text = DateUtils.getCurrentTime()
        }
        viewModel.videoList.observe(requireActivity()) {
            viewModel.getVideoListSize()
            binding.tvVideoTime.text = DateUtils.getCurrentTime()
        }
        viewModel.documentsList.observe(requireActivity()) {
            viewModel.getDocumentsListSize()
            binding.tvDocumentsTime.text = DateUtils.getCurrentTime()
        }
        viewModel.downloadList.observe(requireActivity()) {
            viewModel.getDownloadListSize()
            binding.tvDownloadTime.text = DateUtils.getCurrentTime()
        }
    }

    private fun setMediaListSize() {
        viewModel.imageSpaceSize.observe(requireActivity()) {
            binding.tvImageSpace.text = it
        }
        viewModel.videoSpaceSize.observe(requireActivity()) {
            binding.tvVideoSpace.text = it
        }
        viewModel.audioSpaceSize.observe(requireActivity()) {
            binding.tvAudioSpace.text = it
        }
        viewModel.documentsSpaceSize.observe(requireActivity()) {
            binding.tvDocumentsSpace.text = it
        }
        viewModel.downloadSpaceSize.observe(requireActivity()) {
            binding.tvDownloadSpace.text = it
        }
    }

    override fun getMediaInfo() {
        viewModel.getMemoryInfo()
        viewModel.getImageList(requireContext())
        viewModel.getAudioList(requireContext())
        viewModel.getVideoList(requireContext())
        viewModel.getDocumentsList(requireContext())
        viewModel.getDownloadList(requireContext())
    }

    private fun setClickListener() {
        binding.clFileCommanderAudio.setOnClickListener(this)
        binding.clFileCommanderDocuments.setOnClickListener(this)
        binding.clFileCommanderDownload.setOnClickListener(this)
        binding.clFileCommanderImage.setOnClickListener(this)
        binding.clFileCommanderVideo.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        if (!(requireActivity() as BaseActivity<*, *>).checkPermissionExternal()){
            (requireActivity() as BaseActivity<*, *>).requestPermissionExternal(viewModel)
            return
        }
        when (v?.id) {
            R.id.cl_file_commander_image -> {
                jumpScannerActivity(Common.IMAGE_LIST)
            }

            R.id.cl_file_commander_documents -> {
                jumpScannerActivity(Common.DOCUMENTS_LIST)
            }

            R.id.cl_file_commander_audio -> {
                jumpScannerActivity(Common.AUDIO_LIST)
            }

            R.id.cl_file_commander_video -> {
                jumpScannerActivity(Common.VIDEO_LIST)
            }

            R.id.cl_file_commander_download -> {
                jumpScannerActivity(Common.DOWNLOAD_LIST)
            }
        }
    }

}