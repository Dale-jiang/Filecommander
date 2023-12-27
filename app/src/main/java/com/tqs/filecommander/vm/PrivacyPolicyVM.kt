package com.tqs.filecommander.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.base.BaseVM

class PrivacyPolicyVM : BaseVM() {
    override var title: LiveData<String> = MutableLiveData<String>().apply {
            value = "This is Privacy Policy Fragment"
        }
}