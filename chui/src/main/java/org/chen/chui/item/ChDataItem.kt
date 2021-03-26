package org.chen.chui.item

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView

abstract class ChDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA) {

    private lateinit var adapter: ChAdapter

    var mData: DATA? = null

    init {
        this.mData = data
    }

    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    /**
     * 返回该item布局资源的id
     * */
    open fun getItemLayoutRes(): Int {
        return -1;
    }

    /**
     * 返回该Item的View
     * */
    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    fun setAdapter(adapter: ChAdapter) {
        this.adapter = adapter
    }


    /**
     * 刷新列表*
     * */
    fun refreshItem() {
        adapter.refreshItem(this)
    }

    /**
     * 移除列表
     * */
    fun removeItem() {
        adapter.removeItem(this)
    }


    fun getSpanSize(): Int {
        return 0
    }
}