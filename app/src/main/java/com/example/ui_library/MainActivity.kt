package com.example.ui_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ui_library.demo.ChBannerDemoActivity
import com.example.ui_library.demo.ChTabBottomDemoActivity
import com.example.ui_library.demo.ChTabTopDemoActivity
import com.example.ui_library.demo.ChRefreshDemoActivity
import com.example.ui_library.util.ActivityManager

/**
 *
 * */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityManager.instance.addFontBackCallback(object : ActivityManager.FrontBackCallback {
            override fun onChanged(front: Boolean) {
                Toast.makeText(applicationContext,"当前处于："+front,Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_tab_Bottom -> {
                startActivity(Intent(this, ChTabBottomDemoActivity::class.java))
            }
            R.id.tv_tob_top -> {
                startActivity(Intent(this, ChTabTopDemoActivity::class.java))
            }
            R.id.tv_refresh -> {
                startActivity(Intent(this, ChRefreshDemoActivity::class.java))
            }
            R.id.tv_banner -> {
                startActivity(Intent(this, ChBannerDemoActivity::class.java))
            }
            R.id.tv_front -> {
                startActivity(Intent(this, SecondActivity::class.java))
            }
        }
    }
}