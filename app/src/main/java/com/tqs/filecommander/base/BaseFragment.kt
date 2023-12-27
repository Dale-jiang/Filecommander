package com.tqs.filecommander.base

import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tqs.filecommander.ui.activity.ScannerActivity
import com.tqs.filecommander.utils.Common

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId: Int
    var REQUEST_CODE_PERMISSION = 0x00099
    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            getMediaInfo()
        }
    }
    abstract fun initData()

    open fun jumpScannerActivity(fromPage : String){
        startActivityForResult.launch(Intent(requireActivity(), ScannerActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, fromPage)
        })
    }
    abstract fun getMediaInfo()
}