package com.tqs.filecommander.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.FragmentPrivacyPolicyBinding
import com.tqs.filecommander.base.BaseFragment
import com.tqs.filecommander.vm.PrivacyPolicyVM

class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding, PrivacyPolicyVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_privacy_policy

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initData()
        return root
    }

    override fun initData() {
        viewModel =
            ViewModelProvider(this).get(PrivacyPolicyVM::class.java)
        binding.wv.loadUrl("https://sites.google.com/view/file-commander-privacy-policy/home")

    }

    override fun getMediaInfo() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}