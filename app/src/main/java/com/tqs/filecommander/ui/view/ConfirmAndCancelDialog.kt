package com.tqs.filecommander.ui.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import com.tqs.filecommander.R


class ConfirmAndCancelDialog(
    context: Context,
    private val clickCancel: () -> Unit,
    private val clickSure: () -> Unit
) : Dialog(context) {
    private lateinit var mTitle: TextView
    private lateinit var mContent: TextView
    private lateinit var mCancel: TextView
    private lateinit var mSure: TextView

    private var sTitle: String? = null
    private var sContent: String? = null
    private var sCancel: String? = null
    private var sSure: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_cancel)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        val wm = window?.windowManager
        val defaultDisplay = wm?.defaultDisplay
        val p = window?.attributes
        val point = Point()
        defaultDisplay?.getSize(point)
        p?.width = (point.x * 0.8).toInt()
        window?.attributes = p

        mTitle = findViewById(R.id.dialog_title)
        mContent = findViewById(R.id.dialog_content)
        mCancel = findViewById(R.id.dialog_cancel)
        mSure = findViewById(R.id.dialog_sure)

        mTitle.text = if (TextUtils.isEmpty(sTitle)) {
            "delete selected file"
        } else {
            sTitle
        }
        mContent.text = if (TextUtils.isEmpty(sContent)) {
            "are you sure?"
        } else {
            sContent
        }
        mCancel.text = if (TextUtils.isEmpty(sCancel)) {
            "cancel"
        } else {
            sCancel
        }
        mSure.text = if (TextUtils.isEmpty(sSure)) {
            "sure"
        } else {
            sSure
        }

        mCancel.setOnClickListener {
            clickCancel()
        }
        mSure.setOnClickListener {
            clickSure()
        }
    }

    fun setTitle(string: String): ConfirmAndCancelDialog {
        this.sTitle = string
        return this
    }

    fun setContent(string: String): ConfirmAndCancelDialog {
        this.sContent = string
        return this
    }

    fun setCancel(string: String): ConfirmAndCancelDialog {
        this.sCancel = string
        return this
    }

    fun setSure(string: String): ConfirmAndCancelDialog {
        this.sSure = string
        return this
    }
}