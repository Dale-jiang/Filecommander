package com.tqs.filecommander.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseVM : ViewModel() {

    abstract var title: LiveData<String>
}