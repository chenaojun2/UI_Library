package com.example.ui_library.demo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ui_library.R
import org.chen.chui.tab.bottom.ChTabBottomInfo
import org.chen.chui.tab.bottom.ChTabBottomLayout
import org.chen.cibrary.util.ChDisplayUtil

class ChTabBottomDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ch_tab_bottom_demo2)
        initTabBottom()
    }

    private fun initTabBottom() {
        val chTabBottomLayout:ChTabBottomLayout = findViewById(R.id.ch_tab_layout)
        chTabBottomLayout.setTabAlpha(0.85f)
        val bottomInfoList:MutableList<ChTabBottomInfo<*>> = ArrayList()
        val homeInfo = ChTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = ChTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )

//        val infoCategory = ChTabBottomInfo(
//            "分类",
//            "fonts/iconfont.ttf",
//            getString(R.string.if_category),
//            null,
//            "#ff656667",
//            "#ffd44949"
//        )
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)

        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.fire,null);
        val infoCategory = ChTabBottomInfo<String>(
            "分类",
            bitmap,
            bitmap
        )
        val infoChat = ChTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = ChTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)

        chTabBottomLayout.inflateInfo(bottomInfoList)
        chTabBottomLayout.addTabSelectedChangeListener {_,_,nextInfo->
            Toast.makeText(this@ChTabBottomDemoActivity,nextInfo.name,Toast.LENGTH_LONG).show()
        }
        chTabBottomLayout.defaultSelected(homeInfo)
        //改变某个tab的高度
        val tabBottom = chTabBottomLayout.findTab(bottomInfoList[2])
        tabBottom?.apply { resetHeight(ChDisplayUtil.dp2px(66f,resources)) }
    }
}