package com.tqs.filecommander.ui.activity

import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityNot404Binding
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.vm.MainVM

class Not404Activity(
    override val layoutId: Int = R.layout.activity_not404,
    override val TAG: String = "Not404Activity"
) : BaseActivity<ActivityNot404Binding, MainVM>() {
    override fun initData() {
        binding.notBack.setOnClickListener {
            finish()
        }
        viewModel = ViewModelProvider(this)[MainVM::class.java]
    }

}