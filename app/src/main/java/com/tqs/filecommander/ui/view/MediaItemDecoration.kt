package com.tqs.filecommander.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tqs.filecommander.utils.PixesUtils

class MediaItemDecoration(private val itemSpace: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val spacingPx = PixesUtils.dp2px(parent.context, itemSpace)
        outRect.left = spacingPx
        outRect.right = spacingPx
        outRect.top = spacingPx
        outRect.bottom = spacingPx
    }
}