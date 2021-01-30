package com.example.ui_library.demo

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.ui_library.R
import org.chen.chui.tab.common.IChTabLayout
import org.chen.chui.tab.top.ChTabTopInfo
import org.chen.chui.tab.top.ChTapTopLayout


class ChTabTopDemoActivity : AppCompatActivity() {

    var tabsStr = arrayOf(
        "热门",
        "服装",
        "数码",
        "鞋子",
        "零食",
        "家电",
        "汽车",
        "百货",
        "家居",
        "装修",
        "运动"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ch_tab_top_demo)
        initTabTop()
    }

    private fun initTabTop() {
        val chTabTopLayout: ChTapTopLayout = findViewById(R.id.tab_top_layout)
        val infoList: MutableList<ChTabTopInfo<*>> = ArrayList()
        val defaultColor = resources.getColor(R.color.tabBottomDefaultColor)
        val tintColor = resources.getColor(R.color.tabBottomTintColor)
        for (s in tabsStr) {
            val info: ChTabTopInfo<*> = ChTabTopInfo<Int>(s, defaultColor, tintColor)
            infoList.add(info)
        }
        chTabTopLayout.inflateInfo(infoList)
        chTabTopLayout.addTabSelectedChangeListener { index, prevInfo, nextInfo ->
            Toast.makeText(
                this@ChTabTopDemoActivity,
                nextInfo.name,
                Toast.LENGTH_SHORT
            ).show()
        }
        chTabTopLayout.defaultSelected(infoList[0])
    }
}