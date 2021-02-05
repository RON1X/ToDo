package com.eachubkov.todoapp.utils

import android.content.Context
import android.os.Handler
import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.max
import kotlin.math.min

class GridAutoFitStaggeredLayoutManager
@JvmOverloads constructor(
        private var context: Context?,
        private var columnWidthDp: Int,
        private var maxColumns: Int = 6
): StaggeredGridLayoutManager(setInitialSpanCount(context, columnWidthDp), VERTICAL) {

    companion object {
        private fun setInitialSpanCount(context: Context?, columnWidthDp: Int) : Int {
            val displayMetrics: DisplayMetrics? = context?.resources?.displayMetrics
            return ((displayMetrics?.widthPixels ?: columnWidthDp) / columnWidthDp)
        }
    }

    private var mLastCalculatedWidth = -1

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (width != mLastCalculatedWidth && width > 0) {
            recalculateSpanCount()
        }
        super.onLayoutChildren(recycler, state)
    }

    private fun recalculateSpanCount() {
        val totalSpace: Int = if (orientation == RecyclerView.VERTICAL) {
            width - paddingRight - paddingLeft
        } else {
            height - paddingTop - paddingBottom
        }
        val newSpanCount = min(maxColumns, max(1, totalSpace / columnWidthDp))
        queueSetSpanCountUpdate(newSpanCount)
        mLastCalculatedWidth = width
    }

    private fun queueSetSpanCountUpdate(newSpanCount: Int) {
        if(context != null) {
            Handler(context!!.mainLooper).post { spanCount = newSpanCount }
        }
    }
}