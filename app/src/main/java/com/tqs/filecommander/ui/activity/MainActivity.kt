package com.tqs.filecommander.ui.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.R
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.databinding.ActivityMainBinding
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.notification.NotificationHelper
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.ui.view.TitleBar
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.vm.MainVM

class MainActivity : BaseActivity<ActivityMainBinding, MainVM>(), View.OnClickListener {

    override val layoutId: Int get() = R.layout.activity_main
    override val TAG: String
        get() = this.packageName
    private var navController: NavController? = null

    override fun initData() {
        setContentView(binding.root)
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)?.let {
            navController = Navigation.findNavController(this, it.id)
        }
        navController?.setGraph(R.navigation.mobile_navigation)
        binding.appBarMain.findViewById<TitleBar>(R.id.title_bar_main).setLeftClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.clFileCommander.setOnClickListener(this)
        binding.rlContactUs.setOnClickListener(this)
        binding.rlPrivacyPolicy.setOnClickListener(this)
        binding.rlShare.setOnClickListener(this)
        binding.rlUpgrade.setOnClickListener(this)
        checkAppPermission(viewModel)
    }

    override fun onResume() {
        super.onResume()
        if (System.currentTimeMillis() - MMKVHelper.showMainActivityTime > 30 * 1000) {
            TBAHelper.updateSession()
            MMKVHelper.showMainActivityTime = System.currentTimeMillis()
        }

        TBAHelper.updatePoints(EventPoints.filec_home_show)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_file_commander -> {
                selectedMenu(R.id.nav_file_commander, false)
            }

            R.id.rl_contact_us -> {
                jumpToContactUs()
            }

            R.id.rl_privacy_policy -> {
                selectedMenu(R.id.nav_privacy_policy, false)
            }

            R.id.rl_share -> {
                jumpToShareApp()
            }

            R.id.rl_upgrade -> {
                jumpToUpdate()
            }
        }
    }

    private fun jumpToUpdate() {
        runCatching {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                setPackage("com.android.vending")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun jumpToShareApp() {
        runCatching {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            })
        }
    }

    private fun jumpToContactUs() {
        runCatching {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:${Common.EMAIL}")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    private fun selectedMenu(resID: Int, boolean: Boolean) {
        if (boolean) {
            selectedMenu(resID)
        } else {
            if (navController?.currentDestination?.id != resID) {
                selectedMenu(resID)
            }
        }
    }

    private fun selectedMenu(resID: Int) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        navController?.popBackStack(resID, true)
        navController?.navigate(resID)

    }

    override fun onPermissionSuccess() {
        "onPermissionSuccess".logE()
        selectedMenu(R.id.nav_file_commander, true)
    }

    override fun onBackPressed() {
//        if (System.currentTimeMillis() - viewModel.mainOnBackPressedTime < 300){
            super.onBackPressed()
//        }else{
//            viewModel.mainOnBackPressedTime = System.currentTimeMillis()
//        }
    }

}