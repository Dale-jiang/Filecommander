package com.tqs.filecommander.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.notification.NotificationKey
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.ui.activity.DocListActivity
import com.tqs.filecommander.ui.activity.ImageListActivity
import com.tqs.filecommander.ui.activity.MainActivity
import com.tqs.filecommander.ui.activity.Not404Activity
import com.tqs.filecommander.ui.activity.ScannerActivity
import com.tqs.filecommander.ui.activity.ScannerResultActivity
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.logE
import com.tqs.filecommander.vm.MainVM


abstract class BaseActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId: Int
    abstract val TAG: String
    private var isResume = false
    private val currentNotAllowPermissions: MutableList<String> = mutableListOf()
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->
        currentNotAllowPermissions.clear()

        for ((key, value) in result) {
            if (!value) {
                currentNotAllowPermissions.add(key)
            }
            if (key == Manifest.permission.POST_NOTIFICATIONS) {
                TBAHelper.updatePoints(
                    EventPoints.filec_post_get, mutableMapOf(
                        EventPoints.result to if (value) {
                            "yes"
                        } else {
                            "no"
                        }
                    )
                )
            }
            if (key == Manifest.permission.READ_EXTERNAL_STORAGE || key == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                TBAHelper.updatePoints(
                    EventPoints.filec_premission_result, mutableMapOf(
                        EventPoints.result to if (value) {
                            onPermissionSuccess()
                            "yes"
                        } else {
                            "no"
                        }
                    )
                )
            }

        }

    }


    private val startActivityForResultRequestPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        TBAHelper.updatePoints(
            EventPoints.filec_premission_result, mutableMapOf(
                EventPoints.result to if (checkPermissionExternal()) {
                    onPermissionSuccess()
                    "yes"
                } else {
                    "no"
                }
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCustomDensity()
        binding = DataBindingUtil.setContentView(this, layoutId)
        initData()
        isResume = false
    }

    abstract fun initData()

    private fun setCustomDensity() {
        val metrics = resources.displayMetrics
        (metrics.heightPixels / 760f).let {
            metrics.density = it
            metrics.scaledDensity = it
            metrics.densityDpi = (160 * it).toInt()
        }
    }

    open fun setStatusBarLightMode(activity: Activity, isLightMode: Boolean) {
        val window = activity.window
        var option = window.decorView.systemUiVisibility
        option = if (isLightMode) {
            option or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            option and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = option
    }

    open fun setStatusBarTransparent(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = option
        window.statusBarColor = Color.TRANSPARENT
    }

    fun checkAppPermission(viewModel: MainVM) {
        if (checkPermissionExternal()) {
            onPermissionSuccess()
        } else {
            requestPermissionExternal(viewModel)
        }

        if (!checkPermissionNotification()) {
            requestPermissionNotification()
        }

        if (!checkPermissionOther()) {
            requestPermissionOther()
        }
    }

    private fun checkPermissionOther(): Boolean {
        Common.permissions.toMutableList().forEach {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, it)) {
                currentNotAllowPermissions.add(it)
            }
        }
        return currentNotAllowPermissions.isEmpty()
    }

    private fun requestPermissionOther() {
        "currentNotAllowPermissions = ${currentNotAllowPermissions.size}".logE()
        currentNotAllowPermissions.toTypedArray().forEach {
            it.logE()
        }
        requestPermission.launch(currentNotAllowPermissions.toTypedArray())
    }

    fun checkPermissionExternal(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            Environment.isExternalStorageManager()
        }
    }

    fun requestPermissionExternal(viewModel: MainVM) {
        TBAHelper.updatePoints(EventPoints.filec_premission_show)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            currentNotAllowPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            currentNotAllowPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            requestPermission.launch(currentNotAllowPermissions.toTypedArray())
        } else
            viewModel.showDeleteDialog(this,
                title = "Request Permission",
                content = "Open Permission",
                cancel = {

                }, confirm = {
                    "request permission ExternalManager".logE()
                    requestFilesPermission()
                })

    }

    private fun checkPermissionNotification(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        else
            true
    }

    private fun requestPermissionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            currentNotAllowPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun requestFilesPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            startActivityForResultRequestPermission.launch(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${packageName}")
            })
    }

    open fun onPermissionSuccess() {}
    fun jumpMainAndScannerResultActivity(fromPage: String, notifyType: String?) {
        startActivities(arrayOf(Intent(this, MainActivity::class.java).apply {}, Intent(this, ScannerResultActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, fromPage)
            putExtra(notifyType, notifyType)
        }))
        finish()
    }

    fun jumpScannerResultActivity(fromPage: String, notifyType: String?) {
        startActivity(Intent(this, ScannerResultActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, fromPage)
            putExtra(notifyType, notifyType)
        })
        finish()
    }

    fun jumpMediaListActivity(fromPage: String) {
        val intent = when (fromPage) {
            Common.IMAGE_LIST, Common.VIDEO_LIST -> Intent(this, ImageListActivity::class.java)
            Common.DOCUMENTS_LIST, Common.DOWNLOAD_LIST, Common.AUDIO_LIST -> Intent(this, DocListActivity::class.java)
            else -> Intent(this, Not404Activity::class.java)
        }
        startActivity(intent.apply {
            putExtra(Common.PAGE_TYPE, fromPage)
        })
        finish()
    }

    open fun jumpScannerActivity(fromPage: String, notifyType: String?) {
        startActivity(Intent(this, ScannerActivity::class.java).apply {
            putExtra(Common.PAGE_TYPE, fromPage)
            putExtra(notifyType, notifyType)
        })
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    override fun onStart() {
        super.onStart()
        isResume = false
    }
    override fun onResume() {
        super.onResume()
        isResume = true
    }

    override fun onPause() {
        super.onPause()
        isResume = false
    }

    override fun onStop() {
        super.onStop()
        isResume = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isResume = false
    }

    fun isActivityOnResume():Boolean{
        return isResume && ifLifecycleResume()
    }

    private fun ifLifecycleResume():Boolean{
        return Lifecycle.State.RESUMED == lifecycle.currentState
    }

}