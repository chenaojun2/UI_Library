package com.example.ui_library.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ui_library.R
import org.chen.chui.banner.ChBanner
import org.chen.chui.banner.core.ChBannerMo
import org.chen.chui.banner.indicator.ChCircleIndicator
import org.chen.chui.banner.indicator.ChIndicator

class ChBannerDemoActivity : AppCompatActivity() {

    private var urls = arrayOf(
            "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
            "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
    )
    private var autoPlay: Boolean = false
    private var chIndicator: ChIndicator<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ch_banner_demo)
        initView(ChCircleIndicator(this),false)
        findViewById<Switch>(R.id.auto_play).setOnCheckedChangeListener { _, isChecked ->
            autoPlay = isChecked
            initView(chIndicator,autoPlay)
        }

        findViewById<View>(R.id.tv_switch).setOnClickListener {
            if(chIndicator is ChCircleIndicator){

            }else{
                initView(ChCircleIndicator(this),autoPlay)
            }
        }
    }

    private fun initView(chIndicator: ChIndicator<*>?,autoPlay: Boolean) {
        val mChBanner = findViewById<ChBanner>(R.id.banner)
        val moList: MutableList<ChBannerMo> = ArrayList()
        for (i in 0..7) {
            val mo = BannerMo()
            mo.url = urls[i % urls.size]
            moList.add(mo)
        }

        mChBanner.setChIndicator(chIndicator)
        mChBanner.setAutoPlay(autoPlay)
        mChBanner.setIntervalTime(2000)
        //自定义布局
        mChBanner.setBannerData(R.layout.banner_item_layout, moList)
        mChBanner.setBindAdapter { viewHolder, mo, position ->
            val imageView: ImageView = viewHolder.findViewById(R.id.iv_image)
            Glide.with(this@ChBannerDemoActivity).load(mo.url).into(imageView)
            val titleView: TextView = viewHolder.findViewById(R.id.tv_title)
            titleView.text = mo.url
            Log.d("----position: ", position.toString() + "url:" + mo.url)
        }
    }
}