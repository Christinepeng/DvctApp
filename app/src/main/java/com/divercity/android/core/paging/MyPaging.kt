package com.divercity.android.core.paging

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by lucas on 16/01/2019.
 */

class MyPaging(
    var loadingTriggerThreshold : Int,
    var pageSize : Int,
    var list : RecyclerView,
    var callbacks: Callbacks
) {

    init {
        attachCallbacks()
    }

    private fun attachCallbacks(){
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Just get data when scroll down
                if (dy < 0)
                    checkEndOffset()
            }
        })
    }

    internal fun checkEndOffset() {
        val visibleItemCount = list.childCount
        val totalItemCount = list.layoutManager!!.itemCount

        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition =
                    (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else if (list.layoutManager is StaggeredGridLayoutManager) {
            // https://code.google.com/p/android/issues/detail?id=181461
            firstVisibleItemPosition = if (list.layoutManager!!.childCount > 0) {
                (list.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(
                    null
                )[0]
            } else {
                0
            }
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

        val p = firstVisibleItemPosition + visibleItemCount + loadingTriggerThreshold
        if (p % pageSize == 0) {
            callbacks.onLoadMore()
        } else if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
            callbacks.onLoadMore()
        }
    }

    private constructor(builder: Builder) : this(
        builder.loadingTriggerThreshold,
        builder.pageSize,
        builder.list,
        builder.callbacks)

    companion object {
        inline fun build(list: RecyclerView, callbacks: Callbacks, block: Builder.() -> Unit) = Builder(list, callbacks).apply(block).build()
    }

    class Builder(val list: RecyclerView, val callbacks: Callbacks) {
        var loadingTriggerThreshold = 5
        var pageSize = 30

        fun build() = MyPaging(this)
    }

    interface Callbacks{
        fun onLoadMore()
    }
}